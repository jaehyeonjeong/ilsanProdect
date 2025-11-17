package com.definejae234.cardproject.buylist.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuyListDto {
    int mem_id;
    String mem_userName;
    String mem_userID;
    int card_id;
    String card_name;
    String card_corp;
    String card_image;
    String card_benefit;
    String card_brand;
    LocalDateTime regdate;
}
