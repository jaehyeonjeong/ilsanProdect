package com.definejae234.cardproject.card.service;

import com.definejae234.cardproject.card.dao.CardDao;
import com.definejae234.cardproject.card.dto.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {  // 카드 주요 서비스 기능

    private final CardRepository cardRepository;
    private final CardDao cardDao;

    @Value("${file.path}")
    private String upload;

    // csv scrap mybatis 방식
    // 카드 첫번째 페이지 조건 결과에 따른 리스트 결과
    public List<CardDto> cardCsvFirstPageSelectionList(CardFirstFindPageDto cardFirstFindPageDto) {
        return cardDao.cardCsvFirstPageSelectionList(cardFirstFindPageDto);
    }

    // 카드 정보, 혜택, 브랜드 정보 삭제
    public int deleteCsvCardDataWithID(int id) {
        return cardDao.deleteCsvCardDataWithID(id);
    }

    public int deleteCsvCardBrandDataWithID(int id){
        return cardDao.deleteCsvCardBrandDataWithID(id);
    }

    public int deleteCsvCardBenefitDataWithID(int id) {
        return cardDao.deleteCsvCardBenefitDataWithID(id);
    }

    // 카드 혜택 정보 머지
    public int mergeCsvCardBenefitWithCardTable(CardBenefitDto cardBenefitDto) {
        return cardDao.mergeCsvCardBenefitWithCardTable(cardBenefitDto);
    }

    // 카드 혜택 데이터를 찾기 위한 아이디 파라미터 사용
    public CardBenefitDto findIdByCsvCardBenefitData(int id) {
        return cardDao.findIdByCsvCardBenefitData(id);
    }

    // 카드 브랜드 정보 머지
    public int mergeCsvCardBrandWithCardTable(CardBrandDto cardBrandDto) {
        return cardDao.mergeCsvCardBrandWithCardTable(cardBrandDto);
    }

    // 카드 브랜드 데이터를 찾기 위한 아이디 파라미터 사용
    public CardBrandDto findIdByCsvCardBrandData(int id) {
        return cardDao.findIdByCsvCardBrandData(id);
    }

    // 카드 데이터 업데이트
    public int updateCsvCardTableData(CardDto cardDto) {
        return cardDao.updateCsvCardTableData(cardDto);
    }

    // 아이디에 따른 카드 아이디 찾기
    public CardDto findIdByCsvCardData(int id) {
        return cardDao.findIdByCsvCardData(id);
    }

    // 카드 데이터 저장
    public int insertCsvCardTableData(CardDto cardDto) {
        return cardDao.insertCsvCardTableData(cardDto);
    }

    // 전체 페이지 조회
    public List<CardDto> csvTotalPage() {
        return cardDao.csvTotalPage();
    }

    // myBatis 방식
    public int inputSecondResultTable(CardFirstFindPageDto cardFirstFindPageDto) {
        return cardDao.inputSecondResultTable(cardFirstFindPageDto);
    }

    public int clearSecondResultTable() {
        return cardDao.clearSecondResultTable();
    }

    public List<CardDto> cardListSecondPage(){
        return cardDao.cardListSecondPage();
    }

    public List<CardDto> cardListFirstPageFind(CardFirstFindPageDto cardFirstFindPageDto) {
        return cardDao.cardListFirstPageFind(cardFirstFindPageDto);
    }

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

    public List<CardDto> cardListSecondPage(CardFilterRequestDto filterDto) {
        return cardDao.cardListSecondPage(filterDto);
    }

}
