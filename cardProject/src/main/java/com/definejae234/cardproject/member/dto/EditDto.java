package com.definejae234.cardproject.member.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditDto {
    private String userName;
    private String userEmail;
    private String phone;
    private String zipcode;
    private String address01;
    private String address02;
    private String address03;
}
