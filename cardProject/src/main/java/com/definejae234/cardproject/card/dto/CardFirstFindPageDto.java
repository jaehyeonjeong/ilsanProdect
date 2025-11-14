package com.definejae234.cardproject.card.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardFirstFindPageDto {
    List<String> findBenefitName;       // 카드 조회 첫번쨰 페이지 1번째 조건(카드 혜택 목록)
    private int findBenefitNum;         // 카드 조회 첫번쨰 페이지 2번째 조건(카드 혜택 선택 개수)
    private String findCateName;        // 카드 조회 첫번쨰 페이지 3번째 조건(카드 카테고리 이름)
    private boolean isShareState;       // 카드 조회 첫번째 페이지 4번째 조건(카드 공유 상태 여부)
}
