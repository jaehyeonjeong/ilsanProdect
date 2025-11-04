package com.definejae234.cardproject.card.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="card_brand_table_jpa")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CardBrand {
    @Id
    // 외래키로 Card 클래스의 id를 참조
    @OneToOne(fetch = FetchType.LAZY)   // 해당 테이블은 1대 1관계를 유지한다.
    @JoinColumn(name = "id")   // benefit 테이블의 fk 칼럼명
    private Card card;

    // Getter & Setter
    private boolean visa;
    private boolean master;
    private boolean bc;
    private boolean amex;
}
