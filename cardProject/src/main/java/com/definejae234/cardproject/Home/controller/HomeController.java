package com.definejae234.cardproject.Home.controller;

import com.definejae234.cardproject.buylist.dto.BuyListDto;
import com.definejae234.cardproject.buylist.service.BuyListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final BuyListService buyListService;

    @GetMapping("/")
    public String index(Model model) {
//        model.addAttribute("buyListDto", new BuyListDto());
        List<BuyListDto> buyListDtoList = buyListService.topTenList();
        System.out.println("buyListDtoList = " + buyListDtoList.size());
        model.addAttribute("buyLists", buyListDtoList);
        return "index/index";
    }

}

