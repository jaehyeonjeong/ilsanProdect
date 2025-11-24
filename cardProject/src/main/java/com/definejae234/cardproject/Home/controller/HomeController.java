package com.definejae234.cardproject.Home.controller;

import com.definejae234.cardproject.buylist.dto.BuyListDto;
import com.definejae234.cardproject.buylist.service.BuyListService;
import com.definejae234.cardproject.card.dto.CardDto;
import com.definejae234.cardproject.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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
    public String index(Model model,
                        @RequestParam(value = "size", defaultValue = "5") int size) {
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


        List<CardDto> cardDtoList = cardService.findNewCardList(size);
        model.addAttribute("cardDtoList", cardDtoList);
        // 선택된 size 값을 다시 모델에 담아줌
        model.addAttribute("size", size);

        return "index/index";
    }

    @PostMapping("/")
    @RequestMapping(value="/", method = RequestMethod.POST)
    public RedirectView indexProcess(Model model,
                               @RequestParam(value = "size", defaultValue = "5") int size,
                               RedirectAttributes rttr) {
        List<CardDto> cardDtoList = cardService.findNewCardList(size);
        model.addAttribute("cardDtoList", cardDtoList);
        // 선택된 size 값을 다시 모델에 담아줌
        rttr.addFlashAttribute("message", "등록되었습니다.");
        RedirectView redirectView = new RedirectView("#bottom-section");
        redirectView.setExposeModelAttributes(false);
        return redirectView;
    }
}

