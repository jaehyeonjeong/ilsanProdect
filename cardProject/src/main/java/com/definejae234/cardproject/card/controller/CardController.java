package com.definejae234.cardproject.card.controller;

import com.definejae234.cardproject.buylist.dto.BuyListDto;
import com.definejae234.cardproject.buylist.service.BuyListService;
import com.definejae234.cardproject.card.CardBrandEnum;
import com.definejae234.cardproject.card.CardCateEnum;
import com.definejae234.cardproject.card.CardCorpEnum;
import com.definejae234.cardproject.card.dao.CardDao;
import com.definejae234.cardproject.card.dto.*;
import com.definejae234.cardproject.card.service.CardService;
import com.definejae234.cardproject.member.constant.Role;
import com.definejae234.cardproject.member.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/card")
@SessionAttributes("toggle")
@Slf4j
public class CardController {
    private final CardService cardService;
    private final BuyListService buyListService;
    private final CardDao cardDao;


    @GetMapping("/card/home")
//    @ResponseBody
    public String home() {
        return "card/home";
    }

    @GetMapping("/card/insert")
    public String insert(@ModelAttribute("cardDto") CardDto cardDto,
                         Model model) {
        model.addAttribute("cardDto", new CardDto());
        model.addAttribute("cateEnum", CardCateEnum.values());  // 모든 cardcate값 전달
        model.addAttribute("corpEnum", CardCorpEnum.values());  // 모든 cardCorp값 전달
        return "card/insert";
    }

    @PostMapping("/card/insert")
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
            return "redirect:/admin/list";
        }
        return "card/insert";
    }

    @GetMapping("/admin/list")
    public String list(Model model,
                       @ModelAttribute("pageDto")  UnifiedPageAndCardFilterRequestDto pageDto) {
//        List<CardDto> cardDtoList = cardDao.cardListInfo();
//        List<CardDto> cardDtoList = cardService.cardListInfo(); //mybatis
//        List<Card> cardDtoList = cardService.getAllCards(); // jpa
        pageDto.setTableName("CARD_ADMIN_LIST"); // 테이블 변경 키
        String listPath = "/admin/list";      // 경로
        // 페이지 화면
        int page =  pageDto.getPage();
        int size =  pageDto.getSize();
        int totalCard =  cardDao.totalCard(pageDto); //전체 게시물 수  [csv 데이터 테이블 개수] /10
        int totalPages =  (int)Math.ceil((double)totalCard/size);
        if(totalCard==0) {
            model.addAttribute("cardDtoList",List.of());
            model.addAttribute("responsePageDto",cardService.responseNullPageDto(pageDto));
            return listPath;
        }

        String strResult = cardService.pageRound(page, totalPages, size, listPath);
        if(!strResult.equals("pass")) {
            return strResult;
        }

        model.addAttribute("cardDtoList", cardService.getFindAllCards(pageDto));
        model.addAttribute("responsePageDto",cardService.responsePageDto(pageDto));
        return listPath;
    }

//    @PostMapping("/admin/list")
//    public String listProcess(@RequestParam(value = "benefits", required = false) List<String> brandList,
//                              Model model) {
//
//
//        return "admin/list";
//    }

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

    @GetMapping("/card/{id}/info")
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

    @PostMapping("/card/{id}/info")
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
            return "redirect:/admin/list";
        }

        return "card/" + id + "/info";
    }

    @PostMapping("/card/{id}/delete")
    public String cardDeleteProcess(@PathVariable("id") int id) {
        int deleteBrand = cardService.deleteCsvCardBrandDataWithID(id);
        int deleteBenefit = cardService.deleteCsvCardBenefitDataWithID(id);
        int result;
        if (deleteBrand > 0 && deleteBenefit > 0) {
            result = cardService.deleteCsvCardDataWithID(id);
            if (result > 0) {
                return "redirect:/admin/list";
            }
        }
        return "card/home";
    }

    @GetMapping("/card/script")
    public String cardScript(Model model) {
        return "card/script";
    }

    @GetMapping("/card/firstPage")
    public String cardFirstPage(Model model) {

        return "card/firstPage";
    }


    @PostMapping("/card/firstPage")
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

        model.addAttribute("toggle", true); // 초기값 true로 설정
        return "/card/firstPage";
    }

    @GetMapping("/card/secondPage")
    public String secondPage(Model model,
                             @ModelAttribute("pageDto")  UnifiedPageAndCardFilterRequestDto pageDto
    ) {
        String listPath = "/card/secondPage";      // 경로
        model.addAttribute("minAnnualFee", 0);
        model.addAttribute("maxAnnualFee", 30);
        model.addAttribute("minPreviousPerformance", 0);
        model.addAttribute("maxPreviousPerformance", 50);

        Object toggleAttr = model.getAttribute("toggle");
        if (toggleAttr instanceof Boolean && (Boolean) toggleAttr) {
            // toggle이 true일 때
            pageDto.setTableName("CARD_SECOND_RESULT");
        } else {
            // toggle이 false이거나 존재하지 않을 때
            pageDto.setTableName("CARD_THIRD_RESULT");
        }


        int page =  pageDto.getPage();
        int size =  pageDto.getSize();
        int totalCard =  cardDao.totalCard(pageDto); //전체 게시물 수  [csv 데이터 테이블 개수] /10
        int totalPages =  (int)Math.ceil((double)totalCard/size);
        if(totalCard==0) {
            model.addAttribute("cardDtoList",List.of());
            model.addAttribute("responsePageDto",cardService.responseNullPageDto(pageDto));
            return listPath;
        }

        String strResult = cardService.pageRound(page, totalPages, size, listPath);
        if(!strResult.equals("pass")) {
            return strResult;
        }

        model.addAttribute("cardDtoList", cardService.getFindAllCards(pageDto));
        model.addAttribute("responsePageDto",cardService.responsePageDto(pageDto));
        model.addAttribute("brandEnum02", CardBrandEnum.values());
        model.addAttribute("corpEnum", CardCorpEnum.values()); // 추가: 카드사 Enum
        return listPath;
    }

    @PostMapping("/card/secondPage")
    public String sencondPageProcess(
                                     @ModelAttribute("pageDto")  UnifiedPageAndCardFilterRequestDto pageDto,
                                     Model model) {
        // 필터링 작업을 하는 경우 세번째 테이블을 가져와서 리스트를 호출한다.
        // 대신 세번쨰 테이블을 호출하기 전 세번쨰 테이블을 한번 지우고 다시 생성하는 방식으로 적용한다.
        cardService.clearThirdResultTable();
        cardService.copyFilteredResultsToThird(pageDto);
        model.addAttribute("toggle", false); // 세션에 저장된 toggle 값을 false로 변경

        pageDto.setTableName("CARD_THIRD_RESULT"); // 테이블 변경 키
        String listPath = "/card/secondPage";      // 경로
        // 페이지 화면
        int page =  pageDto.getPage();
        int size =  pageDto.getSize();
        int totalCard =  cardDao.totalCard(pageDto); //전체 게시물 수  [csv 데이터 테이블 개수] /10
        int totalPages =  (int)Math.ceil((double)totalCard/size);
        if(totalCard==0) {
            model.addAttribute("cardDtoList",List.of());
            model.addAttribute("responsePageDto",cardService.responseNullPageDto(pageDto));
            return listPath;
        }

        String strResult = cardService.pageRound(page, totalPages, size, listPath);
        if(!strResult.equals("pass")) {
            return strResult;
        }

        model.addAttribute("cardDtoList", cardService.getFindAllCards(pageDto));
        model.addAttribute("responsePageDto", cardService.responsePageDto(pageDto));

        model.addAttribute("brandEnum02", CardBrandEnum.values());
        model.addAttribute("corpEnum", CardCorpEnum.values());

        model.addAttribute("selectedCardCorpList", pageDto.getCardCorpList());
        model.addAttribute("selectedCardBrandList", pageDto.getCardBrandList());

        model.addAttribute("minAnnualFee", pageDto.getMinAnnualFee());
        model.addAttribute("maxAnnualFee", pageDto.getMaxAnnualFee());
        model.addAttribute("minPreviousPerformance", pageDto.getMinPreviousPerformance());
        model.addAttribute("maxPreviousPerformance", pageDto.getMaxPreviousPerformance());

        return listPath;
    }

    @GetMapping("/card/normal_list")
    public String normalList(Model model,
                             @ModelAttribute("pageDto")  UnifiedPageAndCardFilterRequestDto pageDto) {
        pageDto.setTableName("CARD_TABLE_SCRAP_NORMAL"); // 테이블 변경 키
        String listPath = "/card/normal_list";      // 경로
        // 페이지 화면
        int page =  pageDto.getPage();
        int size =  pageDto.getSize();
        int totalCard =  cardDao.totalCard(pageDto); //전체 게시물 수  [csv 데이터 테이블 개수] /10
        int totalPages =  (int)Math.ceil((double)totalCard/size);
        if(totalCard==0) {
            model.addAttribute("cardDtoList",List.of());
            model.addAttribute("responsePageDto",cardService.responseNullPageDto(pageDto));
            return listPath;
        }

        String strResult = cardService.pageRound(page, totalPages, size, listPath);
        if(!strResult.equals("pass")) {
            return strResult;
        }

        model.addAttribute("cardDtoList", cardService.getFindAllCards(pageDto));
        model.addAttribute("responsePageDto",cardService.responsePageDto(pageDto));
        return listPath;
    }

    @GetMapping("/card/{id}/normal_info")
    public String normalInfo(Model model,
                             @PathVariable("id") int id) {

//        CardNormalInfoDto cardNormalInfoDto = cardService.cardDataNormalInfoById(id); // card_table_jpa
        CardNormalInfoDto cardNormalInfoDto = cardService.cardCsvDataNormalInfoById(id); // scrap table

        model.addAttribute("CardNormalInfoDto", cardNormalInfoDto);

        return "card/normal_info";
    }

    @PostMapping("/card/{id}/normal_info")
    public String normalInfoProcess(Model model,
                                    @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                    @ModelAttribute("CardNormalInfoDto") CardNormalInfoDto cardNormalInfoDto
    ) {
        if (customUserDetails == null || customUserDetails.getLoggedMember() == null) {
            // 로그 출력 및 예외 처리
            System.err.println("로그인 정보가 없습니다.");
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
        String card_cate = cardNormalInfoDto.getCate();
        LocalDateTime regdate = LocalDateTime.now();
        System.out.println("ID : " + mem_ID + " userID : " + userID);

        BuyListDto buyListDto = BuyListDto.builder()
                .mem_id((int) mem_ID.longValue())
                .mem_userID(userID)
                .mem_userName(userName)
                .card_id(card_id)
                .card_name(card_name)
                .card_corp(card_corp)
                .card_cate(card_cate)
                .card_image(card_image)
                .card_benefit(card_benefit)
                .card_brand(card_brand)
                .regdate(regdate)
                .build();

        buyListService.insertBuyList(buyListDto);

        return "redirect:../../card/firstPage";
    }

    @GetMapping("/card/topCrdList")
    public String topCrdList(Model model,
                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Map<String, Object> map = new HashMap<>();
        map.put("cardcate", "CRD");
        map.put("limit", 5);
        List<BuyListDto> buyListDtos = buyListService.topFiveList(map);
        System.out.println("buyLists.size() : " + buyListDtos.size());
        model.addAttribute("buyListDtoList", buyListDtos);
        return "card/topCrdList";
    }

    @GetMapping("/card/topChkList")
    public String topChkList(Model model,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Map<String, Object> map = new HashMap<>();
        map.put("cardcate", "CHK");
        map.put("limit", 5);
        List<BuyListDto> buyListDtos = buyListService.topFiveList(map);
        System.out.println("buyLists.size() : " + buyListDtos.size());
        model.addAttribute("buyListDtoList", buyListDtos);
        return "card/topChkList";
    }
}
