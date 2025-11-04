package com.definejae234.cardproject.member.service;

import com.definejae234.cardproject.member.dto.SignupDto;
import com.definejae234.cardproject.member.entity.Member;
import com.definejae234.cardproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member insertMember(SignupDto signupDto) {
        Member signupMember = Member.builder()
                .userID(signupDto.getUserID())
                .userPW(passwordEncoder.encode(signupDto.getUserPW()))
                .build();
        return memberRepository.save(signupMember);
    }
}
