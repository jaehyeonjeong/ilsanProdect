package com.definejae234.cardproject.buylist.repository;

import com.definejae234.cardproject.buylist.entity.BuyList;
import com.definejae234.cardproject.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuyListRepository extends JpaRepository<BuyList, Long> {
}
