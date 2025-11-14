package com.definejae234.cardproject.card.controller;

import com.definejae234.cardproject.card.CardBrandEnum;
import com.definejae234.cardproject.card.CardCateEnum;
import com.definejae234.cardproject.card.CardCorpEnum;
import com.definejae234.cardproject.card.dao.CardDao;
import com.definejae234.cardproject.card.dto.*;
import com.definejae234.cardproject.card.entity.Card;
import com.definejae234.cardproject.card.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/card")
@Slf4j
public class CardController {
    private final CardService cardService;
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

//        int result = cardService.cardInsertInfo(cardDto); // mybatis 방식
        Card insertedCard = cardService.insertCardInfo(cardDto); // jpa 방식
        log.info("insertedMember==={}", insertedCard);
        System.out.println("cardDto : " + cardDto);
        int findId = cardService.findIdByCardName(cardDto.getName());
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

            cardService.cardBrandMerge(cardBrandDto);
            cardService.cardBenefitMerge(cardBenefitDto);
            return "redirect:/card/list";
        }
        return "card/insert";
    }

    @GetMapping("/list")
    public String list(Model model) {
//        List<CardDto> cardDtoList = cardDao.cardListInfo();
        List<CardDto> cardDtoList = cardService.cardListInfo(); //mybatis
//        List<Card> cardDtoList = cardService.getAllCards(); // jpa
        model.addAttribute("cardDtoList", cardDtoList);
        model.addAttribute("brandEnum", CardBrandEnum.values());
        return "card/list";
    }

    @PostMapping("/list")
    public String listProcess(@RequestParam(value = "benefits",required = false) List<String> brandList,
                              Model model) {
        model.addAttribute("brandEnum", CardBrandEnum.values());

        if(brandList == null){
            List<CardDto> cardDtoList = cardService.cardListInfo(); //mybatis
            model.addAttribute("cardDtoList", cardDtoList);
        } else {
            String strBrand = selectList(brandList);
            CardConditionDto cardConditionDto = CardConditionDto.builder()
                    .cardFindBrand(strBrand)
                    .build();
            List<CardDto> cardDtoList = cardService.cardListInfoByBrand(cardConditionDto); //mybatis
            model.addAttribute("cardDtoList", cardDtoList);
        }

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
                           Model model){
        CardDto cardInfoDto = cardService.cardFindById(id);
        CardBrandDto cardBrandDto = cardService.cardBrandFindById(id);
        CardBenefitDto cardBenefitDto = cardService.cardBenefitFindById(id);
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
                                  Model model){

        cardDto.setId(id); // 안전하게 ID 설정
        int result = cardService.cardUpdateInfo(cardDto);
        int brandResult = cardService.cardBrandMerge(cardBrandDto);
        int benefitResult = cardService.cardBenefitMerge(cardBenefitDto);
        if (result > 0 && brandResult > 0 && benefitResult > 0) {
            return "redirect:/card/list";
        }

        return "card/info";
    }

    @PostMapping("/{id}/delete")
    public String cardDeleteProcess(@PathVariable("id") int id){
        int deleteBrand = cardService.deleteCardBrandById(id);
        int deleteBenefit = cardService.deleteCardBenefitById(id);
        int result;
        if(deleteBrand > 0 && deleteBenefit > 0){
            result = cardService.deleteCardById(id);
            if(result > 0){
                return "redirect:/card/list";
            }
        }
        return "card/home";
    }

    @GetMapping("/script")
    public String cardScript(Model model){
        return "card/script";
    }

    @GetMapping("/firstPage")
    public String cardFirstPage(Model model){
        return "card/firstPage";
    }


    @PostMapping("/firstPage")
    public String cardFirstPageProcess(Model model,
                                       @RequestParam("category") String category,
                                       @RequestParam("benefit") List<String> benefitList
//                                       @RequestParam("countList")
                                       ){
        int benefitCount = benefitList.size(); // 클라이언트 상에 표시된 혜택을 선택한 개수

        // 카드 카테고리랑 혜택 목록중에 선택한 혜택목록을 선택한 경우
        System.out.println("category : " + category + " benefitList : " + benefitList);
        // category : CRD benefitList : [주유, 통신, 쇼핑]
        System.out.println("benefitCount : " + benefitCount);   // 선택한 체크박스 개수

        // 예외처리
        if(benefitList.isEmpty()){
            benefitList.add("%");
        }
        System.out.println("benefitList = " + benefitList);

        CardFirstFindPageDto cardFirstFindPageDto = CardFirstFindPageDto.builder()
                .findBenefitName(benefitList)
                .findBenefitNum(benefitCount)
                .findCateName(category)
                .build();

        List<CardDto> cardDtoList = cardService.cardListFirstPageFind(cardFirstFindPageDto);
        System.out.println("cardDtoList.size() : " + cardDtoList.size());
        int cardListSize = cardDtoList.size();
        model.addAttribute("countList", cardListSize);

        if(cardListSize == 0){
            cardService.clearSecondResultTable();
            return "redirect:/card/firstPage";
        }
        cardService.clearSecondResultTable();
        cardService.inputSecondResultTable(cardFirstFindPageDto);


        return "card/firstPage";
    }

    @GetMapping("/secondPage")
    public String secondPage(Model model) {
        List<CardDto> cardDtoList = cardService.cardListSecondPage(); //mybatis
        model.addAttribute("cardDtoList", cardDtoList);
        return "card/secondPage";
    }
}
