package com.definejae234.cardproject.member.repository;

import com.definejae234.cardproject.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    // MemberEntity와 id를 받는 리포지트리 생성
    Optional<Member> findByUserID(String userID);

    Boolean existsByUserID(String userID);

    Boolean existsByUserEmail(String userEmail);

    Optional<Member> findByUserEmail(String userEmail);

    Page<Member> findByUserIDContainingIgnoreCase(String userID, Pageable pageable);
}
