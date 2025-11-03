package com.definejae234.cardproject.card.dao;

import com.definejae234.cardproject.card.dto.CardBenefitDto;
import com.definejae234.cardproject.card.dto.CardBrandDto;
import com.definejae234.cardproject.card.dto.CardDto;
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
    int findIdByCardName(@Param("name") String name);
}
