package com.definejae234.cardproject.member.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    ROLE_ADMIN("최고관리자" , 10),
    ROLE_MANAGER("중간관리자" , 3),
    ROLE_USER("일반회원" , 1);
    private final String lable;
    private final int lavel;
}
