package com.definejae234.cardproject.card;

import lombok.Getter;

@Getter
public enum CardCateEnum {
    Credit("CRD","신용카드"),        // 신용카드
    Check("CHK", "체크카드");        // 체크카드

    private final String initial;
    private final String kor;
    CardCateEnum(String initial, String kor) {
        this.initial = initial;
        this.kor = kor;
    }
}
