package com.definejae234.cardproject.buylist.service;

import com.definejae234.cardproject.buylist.dao.BuyListDao;
import com.definejae234.cardproject.buylist.dto.BuyListDto;
import com.definejae234.cardproject.buylist.entity.BuyList;
import com.definejae234.cardproject.buylist.repository.BuyListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuyListService {
    private final BuyListRepository buyListRepository;

    // jpa 방식
    public BuyList insertBuyList(BuyListDto buyListDto) {
        BuyList buyList = BuyList.builder()
                .mem_id(buyListDto.getMem_id())
                .mem_userName(buyListDto.getMem_userName())
                .mem_userID(buyListDto.getMem_userID())
                .mem_userPW(buyListDto.getMem_userPW())
                .card_id(buyListDto.getCard_id())
                .card_name(buyListDto.getCard_name())
                .card_corp(buyListDto.getCard_corp())
                .regdate(buyListDto.getRegdate())
                .build();
        return buyListRepository.save(buyList);
    }
}
