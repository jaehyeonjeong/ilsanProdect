package com.definejae234.cardproject.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotBlank(message = "아이디는 필수입력사항입니다.")
    private String userID;
    @NotBlank(message = "패스워드를 확인해주세요")
    private String userPW;
}
