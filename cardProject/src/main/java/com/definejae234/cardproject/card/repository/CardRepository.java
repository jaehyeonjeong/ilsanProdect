package com.definejae234.cardproject.card.repository;

import com.definejae234.cardproject.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card,Long> {

}
