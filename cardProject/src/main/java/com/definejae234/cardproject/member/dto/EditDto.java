package com.definejae234.cardproject.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditDto {
    @NotBlank(message = "이름은 필수 입력사항입니다")
    private String userName;
    @NotBlank(message = "이메일은 필수 입력사항입니다.")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    private String userEmail;
    @NotBlank(message = "전화번호는 필수 입력사항입니다.")
    private String phone;
    private String zipcode;
    private String address01;
    private String address02;
    private String address03;
}
