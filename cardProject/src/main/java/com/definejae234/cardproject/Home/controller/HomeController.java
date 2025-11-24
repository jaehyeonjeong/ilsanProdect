package com.definejae234.cardproject.Home.controller;

import com.definejae234.cardproject.buylist.dto.BuyListDto;
import com.definejae234.cardproject.buylist.service.BuyListService;
import com.definejae234.cardproject.card.dto.CardDto;
import com.definejae234.cardproject.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final BuyListService buyListService;
    private final CardService cardService;

    @GetMapping("/")
    public String index(Model model) {
//        model.addAttribute("buyListDto", new BuyListDto());
        List<BuyListDto> buyListDtoList = buyListService.topTenList();
        System.out.println("buyListDtoList = " + buyListDtoList.size());
        model.addAttribute("buyLists", buyListDtoList);

        Map<String, Object> mapCrd = new HashMap<>();
        Map<String, Object> mapChk = new HashMap<>();
        mapCrd.put("cardcate", "CRD");
        mapCrd.put("limit", 3);

        mapChk.put("cardcate", "CHK");
        mapChk.put("limit", 3);
        List<BuyListDto> buyListCrdDtos = buyListService.topFiveList(mapCrd);
        List<BuyListDto> buyListChkDtos = buyListService.topFiveList(mapChk);
        model.addAttribute("buyListCrdDtos", buyListCrdDtos);
        model.addAttribute("buyListChkDtos", buyListChkDtos);


        List<CardDto> cardDtoList = cardService.findNewCardList();
        model.addAttribute("cardDtoList", cardDtoList);

        return "index/index";
    }

}

