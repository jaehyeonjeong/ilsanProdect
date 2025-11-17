package com.definejae234.cardproject.buylist.dao;

import com.definejae234.cardproject.buylist.dto.BuyListDto;
import com.definejae234.cardproject.buylist.entity.BuyList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BuyListDao {
    List<BuyListDto> findBuylistDataByMemberId(long mem_id);
}
