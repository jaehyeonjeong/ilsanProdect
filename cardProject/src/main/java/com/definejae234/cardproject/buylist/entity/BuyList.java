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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "buylist_seq_gen")
    @SequenceGenerator(
            name = "buylist_seq_gen",
            sequenceName = "buylist_seq",
            allocationSize = 1 // 반드시 1로 설정
    )

    private Long id;

    @Column(name = "mem_id")
    private int mem_id;

    @Column(name = "card_id")
    private int card_id;

    private String mem_userName;
    private String mem_userID;
    private String card_name;
    private String card_corp;
    private String card_image;
    private String card_benefit;
    private String card_brand;
    private LocalDateTime regdate;
}

