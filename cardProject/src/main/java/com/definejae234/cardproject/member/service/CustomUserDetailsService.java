package com.definejae234.cardproject.member.service;

import com.definejae234.cardproject.member.dto.CustomUserDetails;
import com.definejae234.cardproject.member.entity.Member;
import com.definejae234.cardproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository
                .findByUserID(username).orElseThrow();
        return new CustomUserDetails(member);
    }
}
