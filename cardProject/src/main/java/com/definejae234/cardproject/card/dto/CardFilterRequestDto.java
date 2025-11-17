package com.definejae234.cardproject.card.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardFilterRequestDto {
    private List<String> cardCorpList;
    private List<String> cardBrandList;
    private int minAnnualFee;
    private int maxAnnualFee;
    private int minPreviousPerformance;
    private int maxPreviousPerformance;
}
