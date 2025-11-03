package com.definejae234.cardproject.card.service;

import com.definejae234.cardproject.card.dto.CardDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CardServiceTest {
    @Autowired
    CardService cardService;

    @Test
    void cardListInfo() {
        List<CardDto> cardListInfo = cardService.cardListInfo();
        System.out.println(cardListInfo);
        Assertions.assertEquals(1, cardListInfo.size());
    }
}