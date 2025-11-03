package com.definejae234.cardproject.card.controller;

import com.definejae234.cardproject.card.CardCateEnum;
import com.definejae234.cardproject.card.CardCorpEnum;
import com.definejae234.cardproject.card.dao.CardDao;
import com.definejae234.cardproject.card.dto.CardDto;
import com.definejae234.cardproject.card.service.CardService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.smartcardio.Card;
import java.util.List;

@Controller
public class CardController {
    private final CardDao cardDao;
    private final CardService cardService;

    public CardController(CardDao cardDao, CardService cardService) {
        this.cardDao = cardDao;
        this.cardService = cardService;
    }

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

        int result = cardDao.cardInsertInfo(cardDto);

        if(result > 0){
            return "redirect:/card/home";
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
    public String cardInfo(@PathVariable("id") int id, Model model){
        CardDto cardInfoDto = cardDao.cardFindById(id);
        model.addAttribute("cardInfoDto",cardInfoDto);
        return "card/info";
    }

    @PostMapping("/card/{id}/info")
    public String cardInfoProcess(@PathVariable("id") int id,
                                  @ModelAttribute("cardInfoDto") CardDto cardDto,
                                  Model model){

        cardDto.setId(id); // 안전하게 ID 설정
        int result = cardDao.cardUpdateInfo(cardDto);

        if (result > 0) {
            return "redirect:/card/list";
        }

        model.addAttribute("cardInfoDto", cardDto);
        return "card/info";

    }
}
