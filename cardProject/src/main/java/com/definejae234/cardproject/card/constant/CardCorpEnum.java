package com.definejae234.cardproject.card.constant;

import lombok.Getter;

@Getter
public enum CardCorpEnum {
    Kookmin("국민카드"),
    Shinhan("신한카드"),
    Woori("우리카드"),
    Nonghyub("농협카드");

    private final String name;
    CardCorpEnum(String name) {
        this.name = name;
    }

}
