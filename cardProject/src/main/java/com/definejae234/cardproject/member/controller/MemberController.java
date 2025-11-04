package com.definejae234.cardproject.member.controller;

import com.definejae234.cardproject.member.dto.LoginDto;
import com.definejae234.cardproject.member.dto.SignupDto;
import com.definejae234.cardproject.member.entity.Member;
import com.definejae234.cardproject.member.repository.MemberRepository;
import com.definejae234.cardproject.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/member/login")
    public String login(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "member/login";
    }
    @GetMapping("/member/signup")
    public String signup(Model model){
        model.addAttribute("signupDto", new SignupDto());
        return "member/signup";
    }

    @PostMapping("/member/signup")
    public String signupProcess(@Valid @ModelAttribute(name = "signupDto") SignupDto signupDto,
                                BindingResult bindingResult){
        log.info("signDto==={}",signupDto);
        if(bindingResult.hasErrors()){
            return "member/signup";
        }
        Member insertedMember = memberService.insertMember(signupDto);
        log.info("insertedMember==={}",insertedMember);
        return "redirect:/";
    }
}
