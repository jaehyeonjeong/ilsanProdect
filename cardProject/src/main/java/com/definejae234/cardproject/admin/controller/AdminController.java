package com.definejae234.cardproject.admin.controller;

import com.definejae234.cardproject.buylist.dto.BuyListDto;
import com.definejae234.cardproject.buylist.service.BuyListService;
import com.definejae234.cardproject.card.dto.CardDto;
import com.definejae234.cardproject.card.entity.Card;
import com.definejae234.cardproject.card.service.CardService;
import com.definejae234.cardproject.member.entity.Member;
import com.definejae234.cardproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final CardService cardService;
    private final MemberRepository memberRepository;
    private final BuyListService  buyListService;

    @GetMapping("/admin/admin")
    public String admin() {
        return "admin/admin";
    }

    @GetMapping("/admin/members")
    public String memberList(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "") String keyword,
                             Model model) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Member> members;

        if (keyword.isEmpty()) {
            members = memberRepository.findAll(pageable);
        } else {
            members = memberRepository.findByUserIDContainingIgnoreCase(keyword, pageable);
        }

        int totalPages = members.getTotalPages();
        int currentPage = page;

        // ------- 페이징 블럭(5개 단위) 계산 -------
        int blockSize = 5;
        int currentBlock = currentPage / blockSize;

        int startPage = currentBlock * blockSize;
        int endPage = Math.min(startPage + blockSize - 1, totalPages - 1);

        model.addAttribute("members", members);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);

        return "/admin/members";
    }

    @GetMapping("/admin/members/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberRepository.deleteById(id);
        return "redirect:/admin/members";
    }

    @GetMapping("admin/topCrdList_ten")
    public String topCrdList_ten(Model model
    ) {
        List<BuyListDto> buyListDtos = buyListService.topTenList();
        model.addAttribute("buyListDto", buyListDtos);
        return "admin/topCrdList_ten";
    }
}
