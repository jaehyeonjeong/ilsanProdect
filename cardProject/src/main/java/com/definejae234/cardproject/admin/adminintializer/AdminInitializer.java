package com.definejae234.cardproject.admin.adminintializer;

import com.definejae234.cardproject.member.constant.Role;
import com.definejae234.cardproject.member.entity.Member;
import com.definejae234.cardproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    @Override
    public void run(String... args) throws Exception {
        Optional<Member> optionalMember = memberRepository.findByUserID("admin");
        if (!optionalMember.isPresent()){
            Member member = Member.builder()
                    .userID("admin")
                    .userName("최고관리자")
                    .userEmail("최고관리자")
                    .userPW(passwordEncoder.encode("1234"))
                    .role(Role.ROLE_ADMIN)
                    .build();
            memberRepository.save(member);
        }else {
            System.out.println("관리자 계정이 이미 있습니다.");
        }
    }
}
