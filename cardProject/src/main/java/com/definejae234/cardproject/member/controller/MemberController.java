package com.definejae234.cardproject.member.controller;

import com.definejae234.cardproject.member.dto.*;
import com.definejae234.cardproject.member.entity.Member;
import com.definejae234.cardproject.member.repository.MemberRepository;
import com.definejae234.cardproject.member.service.MailService;
import com.definejae234.cardproject.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final MemberRepository memberRepository;

    // 로그인
    @GetMapping("/member/login")
    public String loginForm(Model model, HttpSession session, HttpServletRequest request) {
        model.addAttribute("loginDto", new LoginDto());
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.contains("/login")) {
            session.setAttribute("prevPage", referer);
        }
        return "member/login";
    }
    @PostMapping("/member/login")
    public String loginProcess(@Valid @ModelAttribute LoginDto loginDto,
                               BindingResult bindingResult,
                               HttpSession session,
                               Model model) {

        if(bindingResult.hasErrors()){
            model.addAttribute("loginDto", loginDto);
            return "member/login";
        }

//        Member loggedMember = memberRepository.findByUserID(loginDto.getUserID())
//                .orElseThrow(() -> new IllegalArgumentException("회원정보를 찾을 수 없습니다"));
//        session.setAttribute("loggedMember", loggedMember);

        String prevPage = (String) session.getAttribute("prevPage");
        if(prevPage != null) {
            session.removeAttribute("prevPage");
            return "redirect:" + prevPage;
        }

        return "redirect:/";
    }

    // 회원가입
    @GetMapping("/member/signup")
    public String signup(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        if(customUserDetails != null){
            return "redirect:/";
        }
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

    // 아이디 중복 확인
    @PostMapping("/member/idCheck")
    @ResponseBody
    public Map<String,Boolean> idCheck(@RequestBody SignupDto signupDto){
        Map<String,Boolean> result = new HashMap<>();
        Boolean isDuplicate = memberService.idCheck(signupDto.getUserID());
        result.put("isDuplicate",isDuplicate);
        return result;
    }

    // 아이디 찾기
    @GetMapping("/member/find-id")
    public String findID(Model model) {
        return "member/find-id";
    }
    @PostMapping("/member/find-id")
    public String findIDProcess(@RequestParam("userEmail") String userEmail) {
        mailService.sendFindedIDMain(userEmail);
        return "redirect:/member/login";
    }

    // 비밀번호 찾기
    @GetMapping("/member/find-password")
    public String findPassword(Model model) {
        return "member/find-password";
    }
    @PostMapping("/member/find-password")
    public String findPasswordProcess(@RequestParam("userEmail") String userEmail) {
        mailService.sendChangePasswordMain(userEmail);
        return "redirect:/member/login";
    }

    // 회원 상세정보
    @GetMapping("/member/info")
    public String info(Model model,
                       @AuthenticationPrincipal CustomUserDetails customUserDetails) { // 로그인 후 모든정보가 들어가있음
        model.addAttribute("loggedMember", customUserDetails.getLoggedMember());
        return "member/info";
    }

    // 회원 비밀번호변경
    @GetMapping("/member/change-password")
    public String changePassword(Model model) {
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());
        return "member/change-password";
    }
    @PostMapping("/member/change-password")
    public String changePasswordProcess(@Valid
                                        @ModelAttribute("changePasswordRequest") ChangePasswordRequest changePasswordRequest,
                                        BindingResult bindingResult,
                                        Model model,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                        HttpServletRequest request) {
        if(!changePasswordRequest.isConfirm()){
            bindingResult.rejectValue("newPasswordConfirm","mismatch","새 비밀번호가 일치하지 않습니다");
        }
        String userID = customUserDetails.getLoggedMember().getUserID();
        String currentPassword = changePasswordRequest.getCurrentPassword();
        String newPassword = changePasswordRequest.getNewPassword();
        if (bindingResult.hasErrors()) {
            return "member/change-password";
        }
        try {
            memberService.resetPassword(userID, currentPassword, newPassword);
        }catch (IllegalArgumentException e){
            bindingResult.reject("changePWError",e.getMessage());
            return "member/change-password";
        }
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/member/login";
    }

    // 회원 정보 수정
    @GetMapping("/member/edit")
    public String edit(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Member member = memberRepository.findByUserID(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        String[] addressParts = member.getAddress() != null ? member.getAddress().split("/") : new String[]{"", "", ""};

        EditDto editDto = new EditDto();
        editDto.setUserName(member.getUserName());
        editDto.setUserEmail(member.getUserEmail());
        editDto.setPhone(member.getPhone());
        editDto.setZipcode(member.getZipcode());
        editDto.setAddress01(addressParts.length > 0 ? addressParts[0] : "");
        editDto.setAddress02(addressParts.length > 1 ? addressParts[1] : "");
        editDto.setAddress03(addressParts.length > 2 ? addressParts[2] : "");

        model.addAttribute("editDto", editDto);
        return "member/edit";
    }
    @PostMapping("/member/edit")
    public String editProcess(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @ModelAttribute("editForm") EditDto editDto,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            return "member/edit";
        }

        Member loggedMember = memberRepository.findByUserID(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        loggedMember.applyEditForm(editDto);

        memberRepository.save(loggedMember);

        model.addAttribute("loggedMember", loggedMember);
        return "member/info";
    }

    // 회원탈퇴
    @GetMapping("/member/delete")
    public String delete() {
        return "/member/delete";
    }
    @PostMapping("member/delete")
    public String deleteProcess(@RequestParam(name = "userPW")  String userPW,
                                @AuthenticationPrincipal CustomUserDetails userDetails, Model model,
                                HttpServletRequest request) {
        String userID = userDetails.getUsername();
        Boolean isDelete = memberService.deleteMember(userID,userPW);
        if (isDelete) {
            SecurityContextHolder.clearContext();
            request.getSession(false).invalidate();
            return "redirect:/";
        }
        return "member/delete";
    }
    // 로그아웃
    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
