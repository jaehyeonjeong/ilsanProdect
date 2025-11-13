package com.definejae234.cardproject.buylist.dao;

import com.definejae234.cardproject.buylist.dto.BuyListDto;
import com.definejae234.cardproject.buylist.entity.BuyList;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BuyListDao {
    int insertBuyListData(BuyList buyList);
}
