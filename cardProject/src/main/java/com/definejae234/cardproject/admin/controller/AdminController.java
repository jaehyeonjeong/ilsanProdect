package com.definejae234.cardproject.admin.controller;

import com.definejae234.cardproject.member.entity.Member;
import com.definejae234.cardproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final MemberRepository memberRepository;

    @GetMapping("/admin/admin")
    public String admin() {
        return "admin/admin";
    }

    @GetMapping("/admin/members")
    public String memberList(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "") String keyword,
                             Model model){

        Pageable pageable = PageRequest.of(page,10);
        Page<Member> members;

        if(keyword.isEmpty()){
            members = memberRepository.findAll(pageable);
        }else {
            members = memberRepository.findByUserIDContainingIgnoreCase(keyword,pageable);
        }
        model.addAttribute("members",members);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);

        return "/admin/members";
    }
    @GetMapping("/admin/members/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberRepository.deleteById(id);
        return "redirect:/admin/members";
    }
}

