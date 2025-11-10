package com.definejae234.cardproject.card.service;

import com.definejae234.cardproject.card.dao.CardDao;
import com.definejae234.cardproject.card.dto.CardBenefitDto;
import com.definejae234.cardproject.card.dto.CardBrandDto;
import com.definejae234.cardproject.card.dto.CardConditionDto;
import com.definejae234.cardproject.card.dto.CardDto;
import com.definejae234.cardproject.card.entity.Card;
import com.definejae234.cardproject.card.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {  // 카드 주요 서비스 기능

    private final CardRepository cardRepository;
    private final CardDao cardDao;

    @Value("${file.path}")
    private String upload;

    // myBatis 방식
    public List<CardDto> cardListInfoByBrand(CardConditionDto cardConditionDto) {
        return cardDao.cardListInfoByBrand(cardConditionDto);
    }

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
            Files.createDirectories(Paths.get(upload));     // 폴더의 유무 상관없이 파일 업로드
        } catch (IOException e) {
            throw new RuntimeException("폴더 업로드 실패");
        }

        // 기존 파일 경로와 이름
        String originalFilename = null;
        // 바뀐 파일 경로와 이름
        String renameFileName = null;
        // 썸내일 이미지 이름
        String renameThumbnailFileName = null;

        // 회원가입 시 입력한 파일이름 및 자주사용하는 파일 이름
        MultipartFile cardImage = cardDto.getCardImage();

        if(cardImage!=null && !cardImage.isEmpty()){
            // 파일 처리 이름이 바로 올라감
            originalFilename = cardImage.getOriginalFilename();

            // 1. 확장자 분리
            assert originalFilename != null;
            int dot = originalFilename.lastIndexOf('.');
            String filename = originalFilename.substring(0, dot);
            String extension = originalFilename.substring(dot + 1);
            log.info("filename === {}, extension={}", filename, extension);

            // 2. 업로드할 이미지의 파일이름을 uuid와 같이 넣어 리네임
            String uuid = UUID.randomUUID().toString();
            renameFileName = filename + "_" + uuid + "." + extension;                   // profile_[].ext
            renameThumbnailFileName = filename + "_" + uuid + "_thumb." + extension;    // profile_[]_thumb.ext

            Path mainPath = Path.of(upload, renameFileName); // 리네임 파일명으로 업로드
            Path thumbnailPath = Path.of(upload, renameThumbnailFileName);


            try {
                cardImage.transferTo(mainPath.toFile());  // 메모리에 올리지 않는다.

                // 업로드된 이미지 파일을 읽어서, EXIF 회전 정보를 반영하고, 가로세로 비율을 유지한 채
                // 최대 300x300 크기의 썸네일로 리사이징한 후, 지정된 경로에 저장
                Thumbnails.of(mainPath.toFile())
                        .useExifOrientation(true)
                        .size(300, 300)
                        .crop(Positions.CENTER)             // 만약 사용자 정의 높이 폭대로 맞추려면 crop을 사용
//                        .keepAspectRatio(true)
                        .toFile(thumbnailPath.toFile());    // 썸네일을 경로를 바꿈
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
                .cardImage(renameFileName)
                .renameCardImage(renameThumbnailFileName)
                // 나중에는 이미지 정보도 추가 예정
                .build();
        return cardRepository.save(inputCardInfo);
    }

    // 카드 조회
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

}
