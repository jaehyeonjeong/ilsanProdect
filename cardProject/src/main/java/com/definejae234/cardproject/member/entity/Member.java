package com.definejae234.cardproject.member.entity;

import com.definejae234.cardproject.member.constant.Role;
import com.definejae234.cardproject.member.dto.EditDto;
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
    private String userName;
    @Column(unique = true , nullable = false)
    private String userEmail;
    @Column(unique = true)
    private String phone;
    private String address;
    private String zipcode;
    private String profile;
    private String renameProfile;
    @Enumerated(EnumType.STRING)
    private Role role;


    public void changeUserPW(String encodeUserPW) {
        this.userPW = encodeUserPW;
    }
    public void applyEditForm(EditDto editDto) {
        this.userName = editDto.getUserName();
        this.userEmail = editDto.getUserEmail();
        this.phone = editDto.getPhone();
        this.zipcode = editDto.getZipcode();
        this.address = String.join("/",
                editDto.getAddress01(),
                editDto.getAddress02(),
                editDto.getAddress03());
    }
}
