package com.definejae234.cardproject.card.service;

import com.definejae234.cardproject.card.CardCateEnum;
import com.definejae234.cardproject.card.CardCorpEnum;
import com.definejae234.cardproject.card.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        Assertions.assertNotNull(cardListInfo);
//        Assertions.assertEquals(1, cardListInfo.size());
    }

    @Test
    void cardInsertInfo() {
        CardDto cardDto = CardDto.builder()
                .name("테스트 카드2")
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

    @Test
    void cardFindById() {
        CardDto cardDto = cardService.cardFindById(1);
        assertThat(cardDto).isNotNull();
    }

    @Test
    void cardUpdateInfo() {
        CardDto cardDto = cardService.cardFindById(4);
        cardDto.setName("(수정) 제목");
        int result = cardService.cardUpdateInfo(cardDto);
        assertThat(result).isPositive();
    }

    @Test
    void cardBrandMerge() {
        CardBrandDto cardBrandDto = CardBrandDto.builder()
                .id(26)
                .visa(true)
                .master(true)
                .build();
        int result = cardService.cardBrandMerge(cardBrandDto);
        assertThat(result).isEqualTo(1);
    }

    @Test
    void cardBenefitFindById() {
        CardBenefitDto cardBenefitDto = cardService.cardBenefitFindById(2);
        assertThat(cardBenefitDto).isNotNull();
    }

    @Test
    void cardBenefitMerge() {
        CardBenefitDto cardBenefitDto = CardBenefitDto.builder()
                .id(26)
                .fuel(false)
                .shop(true)
                .comm(false)
                .food(true)
                .cafe(false)
                .build();
        int result = cardService.cardBenefitMerge(cardBenefitDto);
        assertThat(result).isEqualTo(1);
    }

    @Test
    void findIdbyCardName() {
        String name = "Easy All 티타늄카드";
        int result = cardService.findIdByCardName(name);
        System.out.println("result:" + result); // id 값
        assertThat(result).isGreaterThan(0); // 존재 여부만 확인
    }

    @Test
    void deleteCardById() {
        cardService.deleteCardBenefitById(4);

        CardBenefitDto deletedCardBF = cardService.cardBenefitFindById(4);
        assertThat(deletedCardBF).isNull();
//        CardBrandDto deletedCardB = cardService.cardBrandFindById(13);
//        assertThat(deletedCardB).isNull();
//        CardDto deletedCard = cardService.cardFindById(13);
//        assertThat(deletedCard).isNull();
    }

    @Test
    void cardListInfoByBrand() {
        CardConditionDto cardConditionDto = CardConditionDto.builder()
                .cardFindBrand("카페")
                .build();

        List<CardDto> cardListInfo = cardService.cardListInfoByBrand(cardConditionDto);
        System.out.println("cardListInfo.size() : " + cardListInfo.size());
        Assertions.assertNotNull(cardListInfo);
    }

    @Test
    void firstPageFindCard() {
        CardFirstFindPageDto cardFirstFindPageDto = CardFirstFindPageDto.builder()
                .findBenefitName(List.of("통신"))
                .findBenefitNum(1)
                .findCateName("CRD")
                .build();
        List<CardDto> cardDtoList = cardService.cardListFirstPageFind(cardFirstFindPageDto);
        System.out.println("cardDtoList.size() : " + cardDtoList.size());
        Assertions.assertNotNull(cardDtoList);
    }

    @Test
    void clearSecondResultTable() {
        int result = cardService.clearSecondResultTable();
        System.out.println("result:" + result);
//        Assertions.assertEquals(1, result);
    }

    @Test
    void inputSecondResultTable() {
        CardFirstFindPageDto cardFirstFindPageDto = CardFirstFindPageDto.builder()
                .findBenefitName(List.of("통신"))
                .findBenefitNum(1)
                .findCateName("CRD")
                .build();
        int result = cardService.inputSecondResultTable(cardFirstFindPageDto);
        System.out.println("result:" + result);
    }

    @Test
    void cardListSecondPage() {
        List<CardDto> cardDtoList =  cardService.cardListSecondPage();
        System.out.println("cardDtoList.size() : " + cardDtoList.size());
    }
}