package com.definejae234.cardproject.card.service;

import com.definejae234.cardproject.card.dao.CardDao;
import com.definejae234.cardproject.card.dto.CardBenefitDto;
import com.definejae234.cardproject.card.dto.CardBrandDto;
import com.definejae234.cardproject.card.dto.CardDto;
import com.definejae234.cardproject.card.entity.Card;
import com.definejae234.cardproject.card.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {  // 카드 주요 서비스 기능

    private final CardRepository cardRepository;
    private final CardDao cardDao;

    @Value("${file.path}")
    private String upload;

    // myBatis 방식

    public List<CardDto> cardListInfo() {
        return cardDao.cardListInfo();
    }

    public int cardInsertInfo(CardDto cardDto) {
        return cardDao.cardInsertInfo(cardDto);
    }

    public CardDto cardFindById(int id) {
        return cardDao.cardFindById(id);
    }

    public int cardUpdateInfo(CardDto cardDto) {
        return cardDao.cardUpdateInfo(cardDto);
    }

    public CardBrandDto cardBrandFindById(int id) {
        return cardDao.cardBrandFindById(id);
    }

    public int cardBrandMerge(CardBrandDto cardBrandDto) {
        return cardDao.cardBrandMerge(cardBrandDto);
    }

    public CardBenefitDto cardBenefitFindById(int id) {
        return cardDao.cardBenefitFindById(id);
    }

    public int cardBenefitMerge(CardBenefitDto cardBenefitDto) {
        return cardDao.cardBenefitMerge(cardBenefitDto);
    }

    public int findIdByCardName(String name) {
        return cardDao.findIdByCardName(name);
    }

    public int deleteCardById(int id) {
        return cardDao.deleteCardById(id);
    }

    public int deleteCardBenefitById(int id) {
        return cardDao.deleteCardBenefitById(id);
    }

    public int deleteCardBrandById(int id) {
        return cardDao.deleteCardBrandById(id);
    }

    // jpa 방식
    // 카드 삽입
    public Card insertCardInfo(CardDto cardDto) {
        // DB에는 파일 저장이 안되기 때문에 특정 경로의 파일을 업로드하고
        // 업로드된 경로를 DB에 저장해야한다.
        // 그리고 파일경로 자동 생성
        try {
            Files.createDirectories(Paths.get(upload));
        } catch (IOException e) {
            throw new RuntimeException("폴더 업로드 실패");
        }

        // 기존 파일 경로와 이름
        String originalFilename;
        // 바뀐 파일 경로와 이름
        String renameFileName;
        if(cardDto.getCardImage()!=null && !cardDto.getCardImage().isEmpty()){
            // 파일 처리 이름이 바로 올라감
            originalFilename = cardDto.getCardImage().getOriginalFilename();
            assert originalFilename != null;
            Path path = Path.of(upload, originalFilename);
            try {
                Files.write(path, cardDto.getCardImage().getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } // 여기까지가 파일 업로드

        Card inputCardInfo = Card.builder()
                .name(cardDto.getName())
                .cate(cardDto.getCate())
                .annual(cardDto.getAnnual())
                .pre(cardDto.getPre())
                .corp(cardDto.getCorp())
                .discontinue(cardDto.isDiscontinue())
                .sharestate(cardDto.isSharestate())
                // 나중에는 이미지 정보도 추가 예정
                .build();
        return cardRepository.save(inputCardInfo);
    }

    // 카드 조회
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

}
