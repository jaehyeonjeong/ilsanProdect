package com.definejae234.cardproject.card;

import lombok.Getter;

@Getter
public enum CardBenefitEnum {
    fuel("주유"),
    comm("통신"),
    shop("쇼핑"),
    food("음식"),
    cafe("카페");

    private final String name;
    CardBenefitEnum(String name) {
        this.name = name;
    }
}