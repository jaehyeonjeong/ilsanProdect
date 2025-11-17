package com.definejae234.cardproject.card;

import lombok.Getter;

@Getter
public enum CardBrandEnum {
    AMEX("AMEX"),
    BC("BC"),
    MASTER("MASTER"),
    VISA("VISA");

    private final String name;
    CardBrandEnum(String name) {
        this.name = name;
    }
}
