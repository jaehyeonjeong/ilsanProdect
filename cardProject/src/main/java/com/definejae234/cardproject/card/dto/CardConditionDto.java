package com.definejae234.cardproject.card.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardConditionDto {
    private String cardFindBrand;
    private String cardNotFindBrand;
}
