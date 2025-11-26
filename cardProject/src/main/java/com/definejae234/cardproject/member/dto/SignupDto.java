package com.definejae234.cardproject.member.dto;

import com.definejae234.cardproject.member.constant.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {
    private Long id;
    @NotBlank(message = "아이디는 필수 입력사항입니다.")
    private String userID;
    @Size(min=4,message = "최소 4글자 이상입니다.")
    @NotBlank(message = "패스워드는 필수 입력사항입니다.")
    private String userPW;
    @NotBlank(message = "패스워드를 확인해주세요.")
    private String userPWConfirm; // 회원 가입에서만 들어가는 변수
    @NotBlank(message = "이름은 필수 입력사항입니다")
    private String userName;
    @NotBlank(message = "이메일은 필수 입력사항입니다.")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    private String userEmail;
    @NotBlank(message = "전화번호는 필수 입력사항입니다.")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "전화번호는 숫자 10~11자리만 입력 가능합니다.")
    private String phone;
    private String address01;
    private String address02;
    private String address03;
    private String zipcode;
    private MultipartFile profile;
    @Enumerated(EnumType.STRING)
    private Role role;
}
