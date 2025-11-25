package com.definejae234.cardproject.Home.controller;

import com.definejae234.cardproject.buylist.dto.BuyListDto;
import com.definejae234.cardproject.buylist.service.BuyListService;
import com.definejae234.cardproject.card.dto.CardDto;
import com.definejae234.cardproject.card.service.CardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.IOException;
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
                        @RequestParam(value = "size", defaultValue = "5") int size) throws IOException {
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


        // 선택된 size 값을 다시 모델에 담아줌
        model.addAttribute("size", size);

        List<CardDto> cardDtoList = getCardDtoList(size);
        model.addAttribute("cardDtoList", cardDtoList);

        return "index/index";
    }

    @GetMapping("/card-list")
    @ResponseBody
    private List<CardDto> getCardDtoList(@RequestParam(name = "size",defaultValue = "5") int size) throws IOException {
        List<CardDto> cardDtoList = cardService.findNewCardList(size);

        // ObjectMapper 생성
        ObjectMapper mapper = new ObjectMapper();

        // 저장할 경로 지정 (IDE 프로젝트 내 templates/json 폴더)
        File folder = new File("src/main/resources/static/json");
        if (!folder.exists()) {
            folder.mkdirs(); // 폴더 없으면 생성
        }

        // 파일 객체 생성
        File file = new File(folder, "data.json");

        // JSON 파일로 저장
        mapper.writeValue(file, cardDtoList);
        return cardDtoList;
    }
}

