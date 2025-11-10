package com.definejae234.cardproject.card.dao;

import com.definejae234.cardproject.card.dto.CardBenefitDto;
import com.definejae234.cardproject.card.dto.CardBrandDto;
import com.definejae234.cardproject.card.dto.CardDto;
import com.definejae234.cardproject.card.dto.CardConditionDto;
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
}
