package com.definejae234.cardproject.member.service;

import com.definejae234.cardproject.member.constant.Role;
import com.definejae234.cardproject.member.dto.SignupDto;
import com.definejae234.cardproject.member.entity.Member;
import com.definejae234.cardproject.member.repository.MemberRepository;
import com.definejae234.cardproject.member.utils.FileRenameStrategy;
import com.definejae234.cardproject.member.utils.UUIDRenameStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${file.path}")
    private String upload;

    public Member insertMember(SignupDto signupDto) {
        // 여기다가 dto -> entity로 바꿔주는 로직을 작성
        String originalFileName = signupDto.getProfile().getOriginalFilename();
        String renameFile = originalFileName;
        if (originalFileName != null && !originalFileName.isEmpty()) { // 널이 아니고 비어있지 않으면.
            FileRenameStrategy fileRenameStrategy = new UUIDRenameStrategy();
            renameFile = fileRenameStrategy.rename(originalFileName);
            Path path = Paths.get(upload + renameFile);
            try {
                Files.write(path, signupDto.getProfile().getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Member signupMember = Member.builder()
                .userID(signupDto.getUserID())
                .userPW(passwordEncoder.encode(signupDto.getUserPW()))
                .userName(signupDto.getUserName())
                .userEmail(signupDto.getUserEmail())
                .phone(signupDto.getPhone())
                .address(signupDto.getAddress01()+"/"+signupDto.getAddress02()+"/"+signupDto.getAddress03())
                .zipcode(signupDto.getZipcode())
                .profile(signupDto.getProfile().getOriginalFilename())
                .renameProfile(renameFile)
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
    @Transactional
    public Boolean deleteMember(String userID , String userPW) {
        Member member = memberRepository.findByUserID(userID).orElse(null);
        if (member == null){
            return false;
        }
        if(!passwordEncoder.matches(userPW,member.getUserPW())){
            return false;
        }
        memberRepository.delete(member);
        return true;
    }
}
