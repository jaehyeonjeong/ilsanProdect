package com.definejae234.cardproject.card.controller;

import com.definejae234.cardproject.buylist.dto.BuyListDto;
import com.definejae234.cardproject.buylist.service.BuyListService;
import com.definejae234.cardproject.card.CardBenefitEnum;
import com.definejae234.cardproject.card.CardBrandEnum;
import com.definejae234.cardproject.card.CardCateEnum;
import com.definejae234.cardproject.card.CardCorpEnum;
import com.definejae234.cardproject.card.dao.CardDao;
import com.definejae234.cardproject.card.dto.*;
import com.definejae234.cardproject.card.service.CardService;
import com.definejae234.cardproject.member.constant.Role;
import com.definejae234.cardproject.member.dto.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/card")
@Slf4j
public class CardController {
    private final CardService cardService;
    private final BuyListService buyListService;
    private final CardDao cardDao;


    @GetMapping("/home")
//    @ResponseBody
    public String home() {
        return "card/home";
    }

    @GetMapping("/insert")
    public String insert(@ModelAttribute("cardDto") CardDto cardDto,
                         Model model) {
        model.addAttribute("cardDto", new CardDto());
        model.addAttribute("cateEnum", CardCateEnum.values());  // 모든 cardcate값 전달
        model.addAttribute("corpEnum", CardCorpEnum.values());  // 모든 cardCorp값 전달
        return "card/insert";
    }

    @PostMapping("/insert")
    public String insertProcess(@Valid @ModelAttribute("cardDto") CardDto cardDto,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            return "card/insert";
        }
        // 이미지 DTO 처리
        // 저장 경로 설정

//        int result = cardService.cardInsertInfo(cardDto); // mybatis 방식
//        Card insertedCard = cardService.insertCsvCardTableData(cardDto); // jpa 방식
        int result  = cardService.insertCsvCardTableData(cardDto); // csv mybatis 방식
//        log.info("insertedMember==={}", insertedCard);
        System.out.println("cardDto : " + cardDto);
//        System.out.println("cardDto : " + cardDto);
        int findId = cardService.findIdByCsvCardName(cardDto.getName());
        System.out.println("findId : " + findId);

        if (findId > 0) {
            System.out.println("cardDto.getID : " + cardDto.getId());

            // 각 merge 항목에 insert 및 update할 id를 부여
            // 더 좋은 방법이 있는지 연구 필요
            CardBenefitDto cardBenefitDto = CardBenefitDto.builder()
                    .id(findId)
                    .build();

            CardBrandDto cardBrandDto = CardBrandDto.builder()
                    .id(findId)
                    .build();

            cardService.mergeCsvCardBrandWithCardTable(cardBrandDto);
            cardService.mergeCsvCardBenefitWithCardTable(cardBenefitDto);
            return "redirect:/card/list";
        }
        return "card/insert";
    }

    @GetMapping("/list")
    public String list(Model model) {
//        List<CardDto> cardDtoList = cardDao.cardListInfo();
//        List<CardDto> cardDtoList = cardService.cardListInfo(); //mybatis
//        List<Card> cardDtoList = cardService.getAllCards(); // jpa
        List<CardDto> cardDtoList = cardService.csvTotalPage();
        model.addAttribute("cardDtoList", cardDtoList);
        model.addAttribute("brandEnum", CardBenefitEnum.values());
        return "card/list";
    }

    @PostMapping("/list")
    public String listProcess(@RequestParam(value = "benefits", required = false) List<String> brandList,
                              Model model) {
//        model.addAttribute("brandEnum", CardBenefitEnum.values());
//
//        if (brandList == null) {
//            List<CardDto> cardDtoList = cardService.cardListInfo(); //mybatis
//            model.addAttribute("cardDtoList", cardDtoList);
//        } else {
//            String strBrand = selectList(brandList);
//            CardConditionDto cardConditionDto = CardConditionDto.builder()
//                    .cardFindBrand(strBrand)
//                    .build();
//            List<CardDto> cardDtoList = cardService.cardListInfoByBrand(cardConditionDto); //mybatis
//            model.addAttribute("cardDtoList", cardDtoList);
//        }

        return "card/list";
    }

    private static String selectList(List<String> selectList) {
        System.out.println("선택된 benefits:");
        StringBuilder resultBrand = new StringBuilder();
        for (String benefit : selectList) {
            System.out.println(benefit);
            resultBrand.append("|").append(benefit);
        }
        String substring = resultBrand.substring(1, resultBrand.length());

        System.out.println("resultBrand : " + substring);

        return substring;
    }

    @GetMapping("/{id}/info")
    public String cardInfo(@PathVariable("id") int id,
                           Model model) {
        CardDto cardInfoDto = cardService.findIdByCsvCardData(id);
        CardBrandDto cardBrandDto = cardService.findIdByCsvCardBrandData(id);
        CardBenefitDto cardBenefitDto = cardService.findIdByCsvCardBenefitData(id);
        model.addAttribute("cardInfoDto", cardInfoDto);
        model.addAttribute("cardBrandDto", cardBrandDto);
        model.addAttribute("cardBenefitDto", cardBenefitDto);
        return "card/info";
    }

    @PostMapping("/{id}/info")
    public String cardInfoProcess(@PathVariable("id") int id,
                                  @ModelAttribute("cardInfoDto") CardDto cardDto,
                                  @ModelAttribute("cardBrandDto") CardBrandDto cardBrandDto,
                                  @ModelAttribute("cardBenefitDto") CardBenefitDto cardBenefitDto,
                                  Model model) {

        cardDto.setId(id); // 안전하게 ID 설정
        int result = cardService.updateCsvCardTableData(cardDto);
        int brandResult = cardService.mergeCsvCardBrandWithCardTable(cardBrandDto);
        int benefitResult = cardService.mergeCsvCardBenefitWithCardTable(cardBenefitDto);
        if (result > 0 && brandResult > 0 && benefitResult > 0) {
            return "redirect:/card/list";
        }

        return "card/info";
    }

    @PostMapping("/{id}/delete")
    public String cardDeleteProcess(@PathVariable("id") int id) {
        int deleteBrand = cardService.deleteCsvCardBrandDataWithID(id);
        int deleteBenefit = cardService.deleteCsvCardBenefitDataWithID(id);
        int result;
        if (deleteBrand > 0 && deleteBenefit > 0) {
            result = cardService.deleteCsvCardDataWithID(id);
            if (result > 0) {
                return "redirect:/card/list";
            }
        }
        return "card/home";
    }

    @GetMapping("/script")
    public String cardScript(Model model) {
        return "card/script";
    }

    @GetMapping("/firstPage")
    public String cardFirstPage(Model model) {
        return "card/firstPage";
    }


    @PostMapping("/firstPage")
    public String cardFirstPageProcess(Model model,
                                       @RequestParam(value = "category", required = false, defaultValue = "CRD") String category,
                                       @RequestParam(value = "benefit", required = false, defaultValue = "%") List<String> benefitList,
                                       @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Role loggedRole = null;

        try {
            loggedRole = customUserDetails.getLoggedMember().getRole();
            System.out.println("loggedRole: " + loggedRole);
        } catch (NullPointerException e) {
            System.out.println("로그인되지 않은 사용자입니다.");
            // 필요 시 기본 권한 설정 또는 리다이렉트 처리 가능
            // return "redirect:/login"; 또는 return "/card/firstPage";
        }

        int benefitCount = benefitList.size(); // 클라이언트 상에 표시된 혜택을 선택한 개수

        // 카드 카테고리랑 혜택 목록중에 선택한 혜택목록을 선택한 경우
        System.out.println("category : " + category + " benefitList : " + benefitList);
        // category : CRD benefitList : [주유, 통신, 쇼핑]
        System.out.println("benefitCount : " + benefitCount);   // 선택한 체크박스 개수

        // 예외처리
        if (benefitList.isEmpty()) {
            benefitList.add("%");
        }
        System.out.println("benefitList = " + benefitList);

        CardFirstFindPageDto cardFirstFindPageDto = CardFirstFindPageDto.builder()
                .findBenefitName(benefitList)
                .findBenefitNum(benefitCount)
                .findCateName(category)
                .build();


        List<CardDto> cardDtoList;
        if(loggedRole == Role.ROLE_ADMIN){
            // 관리자 전용 카드조회 1페이지 리스트
            cardDtoList = cardService.cardCsvFirstPageSelectionList(cardFirstFindPageDto);
//            cardDtoList = cardService.cardListFirstPageFind(cardFirstFindPageDto);
        } else {
            // 일반회원 전용 카드조회 1페이지 리스트
            cardDtoList = cardService.cardCsvNormalFirstPageSelectionList(cardFirstFindPageDto);
//            cardDtoList = cardService.cardNormalListFirstPageFind(cardFirstFindPageDto);
        }
//        List<CardDto> cardDtoList = cardService.cardListFirstPageFind(cardFirstFindPageDto);
        System.out.println("cardDtoList.size() : " + cardDtoList.size());
        int countList = cardDtoList.size();

        model.addAttribute("countList", countList);

        // 카드 리스트 0개 일 시 기존 second table 조회를 지우기 위함
        if (countList == 0) {
            System.out.println("card Data is empty");
            cardService.clearSecondResultTable();
            return "redirect:/card/firstPage";
        }
        cardService.clearSecondResultTable();

        if(loggedRole == Role.ROLE_ADMIN){
            cardService.inputCsvSecondResultTable(cardFirstFindPageDto);
//            cardService.inputSecondResultTable(cardFirstFindPageDto);
        } else {
            cardService.inputCsvNormalSecondResultTable(cardFirstFindPageDto);
//            cardService.inputSecondResultNormalTable(cardFirstFindPageDto);
        }
//        cardService.inputSecondResultTable(cardFirstFindPageDto);


        return "/card/firstPage";
    }

    @GetMapping("/secondPage")
    public String secondPage(Model model
    ) {
        model.addAttribute("minAnnualFee", 0);
        model.addAttribute("maxAnnualFee", 30);
        model.addAttribute("minPreviousPerformance", 0);
        model.addAttribute("maxPreviousPerformance", 50);
        // 필터링이 없을 때 (초기 접근)는 전체 목록을 로드합니다.
        List<CardDto> cardDtoList = cardService.cardCsvListSecondPage(); // mybatis
//        List<CardDto> cardDtoList = cardService.cardListSecondPage(); // mybatis
        model.addAttribute("cardDtoList", cardDtoList);
        model.addAttribute("brandEnum02", CardBrandEnum.values());
        model.addAttribute("corpEnum", CardCorpEnum.values()); // 추가: 카드사 Enum
        return "card/secondPage";
    }

    @PostMapping("/secondPage")
    public String sencondPageProcess(@ModelAttribute CardFilterRequestDto filterDto,
                                     Model model) {

        //혜택을 선택해서 나온 카드들의 리스트 불러오기
        List<CardDto> cardDtoList = cardService.cardCsvListSecondPage(filterDto);
//        List<CardDto> cardDtoList = cardService.cardListSecondPage(filterDto);

        model.addAttribute("cardDtoList", cardDtoList);
        model.addAttribute("brandEnum02", CardBrandEnum.values());
        model.addAttribute("corpEnum", CardCorpEnum.values());

        model.addAttribute("selectedCardCorpList", filterDto.getCardCorpList());
        model.addAttribute("selectedCardBrandList", filterDto.getCardBrandList());

        model.addAttribute("minAnnualFee", filterDto.getMinAnnualFee());
        model.addAttribute("maxAnnualFee", filterDto.getMaxAnnualFee());
        model.addAttribute("minPreviousPerformance", filterDto.getMinPreviousPerformance());
        model.addAttribute("maxPreviousPerformance", filterDto.getMaxPreviousPerformance());

        return "card/secondPage";
    }

    @GetMapping("/normal_list")
    public String normalList(Model model,
                             @ModelAttribute("pageDto")  PageDto pageDto) {
        pageDto.setTableName("CARD_TABLE_SCRAP_NORMAL"); // 테이블 변경 키
        String listPath = "/card/normal_list";      // 경로
        // 페이지 화면
        int page =  pageDto.getPage();
        int size =  pageDto.getSize();
        int totalCard =  cardDao.totalCard(pageDto); //전체 게시물 수  [csv 데이터 테이블 개수] /10
        int totalPages =  (int)Math.ceil((double)totalCard/size);
        if(totalCard==0) {
            model.addAttribute("cardDtoList",List.of());
            PageDto responsePageDto = PageDto.builder()
                    .page(page)
                    .size(size)
                    .total(totalCard)
                    .totalPages(1)
                    .hasPrev(false)
                    .hasNext(false)
                    .build();
            model.addAttribute("responsePageDto",responsePageDto);
            return listPath;
        }
        if(page < 1) {
            page = 1;
            return "redirect:"+ listPath +"?page="+page+"&size="+size;
        }  //0보다 작아지지 않게....
        if(page > totalPages) {
            page = totalPages;
            return "redirect:"+ listPath +"?page="+page+"&size="+size;
        } // 마지막 보다 커지지 않게...
//        List<CardDto> cardDtoList = cardService.csvNormalTotalPage();   // CARD_TABLE_SCRAP
        List<CardDto> cardDtoList = cardDao.findAll(pageDto);   // CARD_TABLE_SCRAP
        PageDto responsePageDto = PageDto.builder()
                .page(page)
                .size(size)
                .total(totalCard)
                .totalPages(totalPages)
                .hasPrev(page>1)
                .hasNext(page<totalPages)
                .build();

        model.addAttribute("cardDtoList", cardDtoList);
        model.addAttribute("responsePageDto",responsePageDto);
        return "card/normal_list";
    }

    @GetMapping("{id}/normal_info")
    public String normalInfo(Model model,
                             @PathVariable("id") int id) {

//        CardNormalInfoDto cardNormalInfoDto = cardService.cardDataNormalInfoById(id); // card_table_jpa
        CardNormalInfoDto cardNormalInfoDto = cardService.cardCsvDataNormalInfoById(id); // scrap table

        model.addAttribute("CardNormalInfoDto", cardNormalInfoDto);

        return "card/normal_info";
    }

    @PostMapping("{id}/normal_info")
    @ResponseBody
    public String normalInfoProcess(Model model,
                                    @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                    @ModelAttribute("CardNormalInfoDto") CardNormalInfoDto cardNormalInfoDto,
                                    HttpServletRequest request
    ) {
        if (customUserDetails == null || customUserDetails.getLoggedMember() == null) {
            // 로그 출력 및 예외 처리
            System.err.println("로그인 정보가 없습니다.");
            return "로그인 정보가 유효하지 않습니다.";
        }

        Long mem_ID = customUserDetails.getLoggedMember().getId();
        String userID = customUserDetails.getLoggedMember().getUserID();
        String userName = customUserDetails.getLoggedMember().getUserName();
        int card_id = cardNormalInfoDto.getId();
        String card_name = cardNormalInfoDto.getName();
        String card_corp = cardNormalInfoDto.getCorp();
        String card_image = cardNormalInfoDto.getCardimage();
        String card_benefit = cardNormalInfoDto.getBenefits();
        String card_brand = cardNormalInfoDto.getBrands();
        LocalDateTime regdate = LocalDateTime.now();
        System.out.println("ID : " + mem_ID + " userID : " + userID);

        BuyListDto buyListDto = BuyListDto.builder()
                .mem_id((int) mem_ID.longValue())
                .mem_userID(userID)
                .mem_userName(userName)
                .card_id(card_id)
                .card_name(card_name)
                .card_corp(card_corp)
                .card_image(card_image)
                .card_benefit(card_benefit)
                .card_brand(card_brand)
                .regdate(regdate)
                .build();

        buyListService.insertBuyList(buyListDto);

        return "구매 완료";
    }

}
