package com.definejae234.cardproject.member.controller;

import com.definejae234.cardproject.member.dto.LoginDto;
import com.definejae234.cardproject.member.dto.SignupDto;
import com.definejae234.cardproject.member.entity.Member;
import com.definejae234.cardproject.member.repository.MemberRepository;
import com.definejae234.cardproject.member.service.MailService;
import com.definejae234.cardproject.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MailService mailService;

    @GetMapping("/member/login")
    public String login(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "member/login";
    }
    @GetMapping("/member/signup")
    public String signup(Model model){
        model.addAttribute("signupDto",new SignupDto());
        return "member/signup";
    }

    @PostMapping("/member/signup")
    public String signupProcess(@Valid @ModelAttribute(name = "signupDto") SignupDto signupDto,
                                BindingResult bindingResult){
        log.info("signDto==={}",signupDto);
        if(bindingResult.hasErrors()){
            return "member/signup";
        }
        if(memberService.idCheck(signupDto.getUserID())){
            bindingResult
                    .rejectValue("userID","duplicateID","이미 사용중인 아이디입니다");
            return "member/signup";
        }
        if (memberService.emailCheck(signupDto.getUserEmail())) {
            bindingResult.rejectValue("userEmail", "duplicateEmail", "이미 사용중인 이메일입니다");
            return "member/signup";
        }
        Member insertedMember = memberService.insertMember(signupDto);
        log.info("insertedMember==={}",insertedMember);
        return "redirect:/";
    }
    @PostMapping("/member/idCheck")
    @ResponseBody
    public Map<String,Boolean> idCheck(@RequestBody SignupDto signupDto){
        Map<String,Boolean> result = new HashMap<>();
        Boolean isDuplicate = memberService.idCheck(signupDto.getUserID());
        result.put("isDuplicate",isDuplicate);
        return result;
    }
    @GetMapping("/find-id")
    public String findID(Model model) {
        return "member/find-id";
    }

    @PostMapping("/find-id")
    public String findIDProcess(@RequestParam("userEmail") String userEmail) {
        mailService.sendFindedIDMain(userEmail);
        return "redirect:/member/login";
    }
}
