package com.definejae234.cardproject.buylist.controller;

import com.definejae234.cardproject.buylist.dto.BuyListDto;
import com.definejae234.cardproject.buylist.service.BuyListService;
import com.definejae234.cardproject.member.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("buylist")
@RequiredArgsConstructor
public class BuyListController {
    private final BuyListService buyListService;

    @GetMapping("/mylist")
    public String mylist(Model model,
                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        int mem_id = (int) customUserDetails.getLoggedMember().getId().longValue();
        List<BuyListDto> buyLists = buyListService.findBuylistDataByMemberId(mem_id);
        model.addAttribute("buyLists", buyLists);
        return "buylist/mylist";
    }
}
