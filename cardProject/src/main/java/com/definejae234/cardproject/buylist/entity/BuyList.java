package com.definejae234.cardproject.buylist.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "member_card_buylist_jpa")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BuyList {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "mem_id")
    private int mem_id;

    @Column(name = "card_id")
    private int card_id;

    private String mem_userName;
    private String mem_userID;
    private String mem_userPW;
    private String card_name;
    private String card_corp;
    private LocalDateTime regdate;
}

