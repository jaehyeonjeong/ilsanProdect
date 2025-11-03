package com.definejae234.cardproject.card.controller;

import com.definejae234.cardproject.card.CardCateEnum;
import com.definejae234.cardproject.card.CardCorpEnum;
import com.definejae234.cardproject.card.dao.CardDao;
import com.definejae234.cardproject.card.dto.CardBenefitDto;
import com.definejae234.cardproject.card.dto.CardBrandDto;
import com.definejae234.cardproject.card.dto.CardDto;
import com.definejae234.cardproject.card.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.smartcardio.Card;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;


    @GetMapping("/card/home")
//    @ResponseBody
    public String home(){
        return "card/home";
    }

    @GetMapping("/card/insert")
    public String insert(@ModelAttribute("cardDto") CardDto cardDto,
                         Model model){
        model.addAttribute("cardDto",new CardDto());
        model.addAttribute("cateEnum", CardCateEnum.values());  // 모든 cardcate값 전달
        model.addAttribute("corpEnum", CardCorpEnum.values());  // 모든 cardCorp값 전달
        return "card/insert";
    }

    @PostMapping("/card/insert")
    public String insertProcess(@Valid @ModelAttribute("cardDto") CardDto cardDto,
                                BindingResult bindingResult,
                                Model model){
        if(bindingResult.hasErrors()){
            return "card/insert";
        }

        int result = cardService.cardInsertInfo(cardDto);
        System.out.println("cardDto : " + cardDto);
        int findId = cardService.findIdByCardName(cardDto.getName());

        if(result > 0){
            System.out.println("cardDto.getID : " + cardDto.getId());

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

    @GetMapping("/card/list")
    public String list(Model model){
//        List<CardDto> cardDtoList = cardDao.cardListInfo();
        List<CardDto> cardDtoList = cardService.cardListInfo();
        model.addAttribute("cardDtoList",cardDtoList);
        return "card/list";
    }

    @GetMapping("/card/{id}/info")
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

    @PostMapping("/card/{id}/info")
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

    @PostMapping("/card/{id}/delete")
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
}
