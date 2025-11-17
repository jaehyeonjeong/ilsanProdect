package com.definejae234.cardproject.buylist.service;

import com.definejae234.cardproject.buylist.dao.BuyListDao;
import com.definejae234.cardproject.buylist.dto.BuyListDto;
import com.definejae234.cardproject.buylist.entity.BuyList;
import com.definejae234.cardproject.buylist.repository.BuyListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuyListService {
    private final BuyListRepository buyListRepository;
    private final BuyListDao buyListDao;

    // jpa 방식
    public BuyList insertBuyList(BuyListDto buyListDto) {
        BuyList buyList = BuyList.builder()
                .mem_id(buyListDto.getMem_id())
                .mem_userName(buyListDto.getMem_userName())
                .mem_userID(buyListDto.getMem_userID())
                .card_id(buyListDto.getCard_id())
                .card_name(buyListDto.getCard_name())
                .card_corp(buyListDto.getCard_corp())
                .card_image(buyListDto.getCard_image())
                .card_benefit(buyListDto.getCard_benefit())
                .card_brand(buyListDto.getCard_brand())
                .regdate(buyListDto.getRegdate())
                .build();
        return buyListRepository.save(buyList);
    }


    // mybatis 방식
    public List<BuyListDto> findBuylistDataByMemberId(int mem_id) {
        return buyListDao.findBuylistDataByMemberId(mem_id);
    }
}
