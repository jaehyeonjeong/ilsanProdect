package com.definejae234.cardproject.card.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UnifiedPageAndCardFilterRequestDto  extends PageDto{//세로관련

    private List<String> cardCorpList;
    private List<String> cardBrandList;
    private int minAnnualFee;
    private int maxAnnualFee;
    private int minPreviousPerformance;
    private int maxPreviousPerformance;
}
