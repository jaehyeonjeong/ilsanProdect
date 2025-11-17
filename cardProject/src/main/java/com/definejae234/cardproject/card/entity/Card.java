package com.definejae234.cardproject.card.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.apache.ibatis.type.NStringTypeHandler;

@Entity
@SequenceGenerator(
        name="CARD_ID_SEQ_GENERATOR",
        sequenceName = "card_id_seq_jpa",
        initialValue=1,
        allocationSize=1
)

@Table(name="card_table_jpa")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "CARD_ID_SEQ_GENERATOR")
    private Long id;                     // 카드 아이디 (따로 valid 하지 않아도 됨, DB SEQ)

    @NotBlank(message="카드 이름은 필수 입력 사항 입니다.")
    @Column(nullable = false, unique = true)
    private String name;                // 카드 이름
    @NotBlank(message="카드 타입은 필수 입력 사항 입니다.")
    private String cate;                // 카드 타입(신용카드 : CRD, 체크카드 : CHK, Enum으로 테스트)
    private int annual;                 // 카드 연회비
    private int pre;                    // 카드 전원 실적
    @NotBlank(message="카드 회사는 필수 입력 사항 입니다.")
    private String corp;                // 카드 회사 => 이건 Enum으로 따로 저장할 수 있는지 테스트
    //    private int rank;                   // 카드 등급 => 아마 카드 구매 등급은 member에서 많이 가지고 있는 순으로 봐야 할 듯
    @Column(nullable = true)
    private boolean discontinue;            // 카드 단종여부 (1 이면 단종, 0이면 단종, 기본은 0)
    @Column(nullable = true)
    private boolean sharestate = false;             // 카드 공유 여부 (1이면 공유 아니면 공유X 기본은 0)
    private String cardImage;                   // 카드 이미지 이름
    private String renameCardImage;             // 재정의 된 카드 이미지 이름 (덮어쓰기 및 충돌 방지)
}
