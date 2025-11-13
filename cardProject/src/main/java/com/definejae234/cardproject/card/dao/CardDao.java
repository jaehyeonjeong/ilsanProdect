package com.definejae234.cardproject.card.dao;

import com.definejae234.cardproject.card.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface CardDao {  // mapper/card.xml id로 연동할 함수 나열
    int cardInsertInfo(CardDto cardDto);
    List<CardDto> cardListInfo();
    CardDto cardFindById(int id);
    int cardUpdateInfo(CardDto cardDto);
    CardBrandDto cardBrandFindById(int id);
    int cardBrandMerge(CardBrandDto cardBrandDto);
    CardBenefitDto cardBenefitFindById(int id);
    int cardBenefitMerge(CardBenefitDto cardBenefitDto);

    // @Param을 붙인 이유는 mapper의 파라미터 이름을 표시하기 위함
    int findIdByCardName(@Param("name") String name);
    int deleteCardById(@Param("id") int id);
    int deleteCardBenefitById(@Param("id") int id);
    int deleteCardBrandById(@Param("id") int id);

    List<CardDto> cardListInfoByBrand(CardConditionDto cardConditionDto);
    List<CardDto> cardListFirstPageFind(CardFirstFindPageDto cardFirstFindPageDto);

    // 카드 조회 첫번째 페이지에서 찾은 두번째 데이터 테이블 리스트
    int clearSecondResultTable();
    int inputSecondResultTable(CardFirstFindPageDto cardFirstFindPageDto);
    List<CardDto> cardListSecondPage();

    //카드 조회 첫번째 페이지에서 필터를 적용하여 출력하는 리스트
    List<CardDto> cardListSecondPage(CardFilterRequestDto filterDto);
}
