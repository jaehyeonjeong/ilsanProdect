package com.definejae234.cardproject.member.dto;

import com.definejae234.cardproject.member.constant.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {
    private Long id;
    @NotBlank(message = "아이디는 필수입력사항입니다.")
    private String userID;
    @NotBlank(message = "패스워드는 필수입력사항입니다.")
    private String userPW;
    @NotBlank(message = "패스워드를 확인해주세요")
    private String userPWConfirm; // 회원 가입에서만 들어가는 변수
    private String userName;
    private String userEmail;
    private String phone;
    private String address01;
    private String address02;
    private String address03;
    private String zipcode;
    private MultipartFile profile;
    @Enumerated(EnumType.STRING)
    private Role role;
}
