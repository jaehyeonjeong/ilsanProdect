package com.definejae234.cardproject.card.dao;

import com.definejae234.cardproject.card.dto.CardDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CardDao {
    int cardInsertInfo(CardDto cardDto);
}
