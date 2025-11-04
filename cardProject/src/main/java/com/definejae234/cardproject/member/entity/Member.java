package com.definejae234.cardproject.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@SequenceGenerator(
        name="MEMBER_SEQ_GENERATOR",
        sequenceName = "member_card_seq_jpa",
        initialValue=1,
        allocationSize=1
)

@Table(name="member_card_jpa")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "MEMBER_SEQ_GENERATOR")
    private Long id;            // primary key
    @Column(unique = true, nullable = false)
    private String userID;      // unique, not null
    private String userPW;      // not null
}
