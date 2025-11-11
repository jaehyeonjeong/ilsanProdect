package com.jjyy0614.jpa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    @NotBlank(message = "현재 비밀번호를 쓰세요")
    private String currentPassword;
    @NotBlank(message = "새 비밀번호를 쓰세요")
    private String newPassword;
    @NotBlank(message = "새 비밀번호 확인해 주세요")
    private String newPasswordConfirm;

    public boolean isConfirm(){
        return newPassword != null && newPasswordConfirm.equals(newPassword);
    }
}