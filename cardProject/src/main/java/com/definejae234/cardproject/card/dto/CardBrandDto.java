package com.definejae234.cardproject.card.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardBrandDto {
    private int id;
    private int visa;
    private int master;
    private int bc;
    private int amex;
}
