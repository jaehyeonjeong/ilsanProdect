package com.definejae234.cardproject.buylist.service;

import com.definejae234.cardproject.buylist.dto.BuyListDto;
import com.definejae234.cardproject.buylist.entity.BuyList;
import com.definejae234.cardproject.card.service.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class BuyListServiceTest {

    @Autowired
    BuyListService buyListService;

    @Test
    void insertBuyList() {
        BuyListDto buyListDto = BuyListDto.builder()
                .mem_id(4)
                .mem_userName("정재현")
                .mem_userID("definejae234")
                .mem_userPW("1234")
                .card_id(1)
                .card_name("국민카드 체크")
                .card_corp("국민카드")
                .regdate(LocalDateTime.now())
                .build();
        BuyList buyList = buyListService.insertBuyList(buyListDto);
        System.out.println(buyList);
    }
}