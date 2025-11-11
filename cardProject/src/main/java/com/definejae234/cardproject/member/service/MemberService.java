package com.definejae234.cardproject.member.service;

import com.definejae234.cardproject.member.constant.Role;
import com.definejae234.cardproject.member.dto.SignupDto;
import com.definejae234.cardproject.member.entity.Member;
import com.definejae234.cardproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member insertMember(SignupDto signupDto) {
        Member signupMember = Member.builder()
                .userID(signupDto.getUserID())
                .userPW(passwordEncoder.encode(signupDto.getUserPW()))
                .userName(signupDto.getUserName())
                .userEmail(signupDto.getUserEmail())
                .phone(signupDto.getPhone())
                .address(signupDto.getAddress01()+" "+signupDto.getAddress02()+" "+signupDto.getAddress03())
                .zipcode(signupDto.getZipcode())
                .role(Role.ROLE_USER)
                .build();
        return memberRepository.save(signupMember);
    }
    public Boolean idCheck(String userID) {
        return memberRepository.existsByUserID(userID);
    }
    public Boolean emailCheck(String userEmail) {
        return memberRepository.existsByUserEmail(userEmail);
    }
    @Transactional
    public void changePassword(String randomNum, String userEmail) {
        Optional<Member> optionalMember = memberRepository.findByUserEmail(userEmail);
        if(optionalMember.isPresent()){
            Member findedMember = optionalMember.get();
            findedMember.changeUserPW(passwordEncoder.encode(randomNum));
        }
    }
    @Transactional
    public String findedByUserID(String userEmail){
        Optional<Member> optionalMember = memberRepository.findByUserEmail(userEmail);
        if(optionalMember.isPresent()){
            Member findedMember = optionalMember.get();
            return findedMember.getUserID();
        }else {
            return null;
        }
    }
    @Transactional
    public void resetPassword(String userID,String currentPassword,String newPassword){
        Member findedMember = memberRepository.findByUserID(userID).orElse(null);
        if(!passwordEncoder.matches(currentPassword,findedMember.getUserPW())){
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }
        if(passwordEncoder.matches(newPassword,findedMember.getUserPW())){
            throw new IllegalArgumentException("이전과 같은 비밀번호는 사용할 수 없습니다.");
        }
        findedMember.changeUserPW(passwordEncoder.encode(newPassword));
    }
}
