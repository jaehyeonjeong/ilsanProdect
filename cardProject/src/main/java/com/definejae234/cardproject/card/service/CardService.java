package com.definejae234.cardproject.card.service;

import com.definejae234.cardproject.card.dao.CardDao;
import com.definejae234.cardproject.card.dto.CardBenefitDto;
import com.definejae234.cardproject.card.dto.CardBrandDto;
import com.definejae234.cardproject.card.dto.CardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {  // 카드 주요 서비스 기능

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

    public CardBrandDto cardBrandFindById(int id){
        return cardDao.cardBrandFindById(id);
    }

    public int cardBrandMerge(CardBrandDto cardBrandDto) {
        return cardDao.cardBrandMerge(cardBrandDto);
    }

    public CardBenefitDto cardBenefitFindById(int id){
        return cardDao.cardBenefitFindById(id);
    }

    public int cardBenefitMerge(CardBenefitDto cardBenefitDto) {
        return cardDao.cardBenefitMerge(cardBenefitDto);
    }

    public int findIdByCardName(String name){
        return cardDao.findIdByCardName(name);
    }

    public int deleteCardById(int id) {
        return cardDao.deleteCardById(id);
    }

    public int deleteCardBenefitById(int id) {
        return cardDao.deleteCardBenefitById(id);
    }

    public int deleteCardBrandById(int id) {
        return cardDao.deleteCardBrandById(id);
    }
}
