package com.definejae234.cardproject.buylist.dao;

import com.definejae234.cardproject.buylist.dto.BuyListDto;
import com.definejae234.cardproject.buylist.entity.BuyList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BuyListDao {
    List<BuyListDto> findBuylistDataByMemberId(long mem_id);
    List<BuyListDto> topFiveList(Map<String, Object> limit);
}
