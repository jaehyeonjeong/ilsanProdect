package com.definejae234.cardproject.card.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardBenefitDto {
    private int id;
    private boolean fuel;
    private boolean comm;
    private boolean shop;
    private boolean food;
    private boolean cafe;
}
