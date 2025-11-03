package com.definejae234.cardproject.card.service;

import com.definejae234.cardproject.card.CardCateEnum;
import com.definejae234.cardproject.card.CardCorpEnum;
import com.definejae234.cardproject.card.dto.CardDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional      // 자동 롤백
class CardServiceTest {
    @Autowired
    CardService cardService;

    @Test
    void cardListInfo() {
        List<CardDto> cardListInfo = cardService.cardListInfo();
        System.out.println(cardListInfo);
        Assertions.assertEquals(1, cardListInfo.size());
    }

    @Test
    void cardInsertInfo() {
        CardDto cardDto = CardDto.builder()
                .name("테스트 카드")
                .cate(CardCateEnum.Check.name())
                .annual(10)
                .pre(30)
                .corp(CardCorpEnum.Kookmin.getName())
                .build();
        int result = cardService.cardInsertInfo(cardDto);
        Assertions.assertEquals(1, result);
    }


    @Test
    void test01() {
        int result = 100 + 10;
        assertThat(result)
                .isPositive()
                .isEqualTo(110)
                .isGreaterThan(0);
    }
}