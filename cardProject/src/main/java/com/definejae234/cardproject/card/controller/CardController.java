package com.definejae234.cardproject.card.controller;

import com.definejae234.cardproject.card.dao.CardDao;
import com.definejae234.cardproject.card.dto.CardDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CardController {
    private final CardDao cardDao;

    public CardController(CardDao cardDao) {
        this.cardDao = cardDao;
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
}
