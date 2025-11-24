package com.definejae234.cardproject.sequence;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class SequenceInitializer implements ApplicationRunner {
    private final EntityManager entityManager;

    @Override
    public void run(ApplicationArguments args) {
        // 테이블의 현재 최대 id 조회
        Long maxId = (Long) entityManager.createQuery(
                        "SELECT COALESCE(MAX(b.id), 0) FROM BuyList b")
                .getSingleResult();

//        System.out.println("maxId: " + maxId);

        // 현재 시퀀스 값 조회
        BigDecimal currentVal = (BigDecimal) entityManager
                .createNativeQuery("SELECT buylist_seq.nextval FROM dual")
                .getSingleResult();
//        System.out.println("currentVal: " + currentVal);

        // 시퀀스가 maxId보다 작으면 올려줌
        if (currentVal.longValue() <= maxId) {
            long diff = maxId - currentVal.longValue() + 1;
            entityManager.createNativeQuery("ALTER SEQUENCE buylist_seq INCREMENT BY " + diff).executeUpdate();
            entityManager.createNativeQuery("SELECT buylist_seq.nextval FROM dual").getSingleResult();
            entityManager.createNativeQuery("ALTER SEQUENCE buylist_seq INCREMENT BY 1").executeUpdate();
        }
    }

}
