package com.definejae234.cardproject.card.dao;

import com.definejae234.cardproject.card.dto.CardBrandDto;
import com.definejae234.cardproject.card.dto.CardDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CardDao {
    int cardInsertInfo(CardDto cardDto);
    List<CardDto> cardListInfo();
    CardDto cardFindById(int id);
    int cardUpdateInfo(CardDto cardDto);
    CardBrandDto cardBrandFindById(int id);
    int cardBrandMerge(CardBrandDto cardBrandDto);
}
