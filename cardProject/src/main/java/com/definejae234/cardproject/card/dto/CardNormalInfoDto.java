package com.definejae234.cardproject.card.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardNormalInfoDto {
    public int id;
    public String name;
    public String corp;
    public int annual;
    public int pre;
    public String benefits;
    public String brands;
    public String cardimage;
    public String cate;
    public boolean discontinue;
}
