package com.definejae234.cardproject.card.service;

import com.definejae234.cardproject.card.dao.CardDao;
import com.definejae234.cardproject.card.dto.CardBrandDto;
import com.definejae234.cardproject.card.dto.CardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardDao cardDao;

    public List<CardDto> cardListInfo() {
        return cardDao.cardListInfo();
    }

    public int cardInsertInfo(CardDto cardDto) {
        return cardDao.cardInsertInfo(cardDto);
    }

    public CardDto cardFindById(int id){
        return cardDao.cardFindById(id);
    }

    public int cardUpdateInfo(CardDto cardDto) {
        return cardDao.cardUpdateInfo(cardDto);
    }

    public int cardBrandMerge(CardBrandDto cardBrandDto) {
        return cardDao.cardBrandMerge(cardBrandDto);
    }
}
