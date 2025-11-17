package com.definejae234.cardproject.card.service;

import com.definejae234.cardproject.card.CardCateEnum;
import com.definejae234.cardproject.card.CardCorpEnum;
import com.definejae234.cardproject.card.dto.*;
import com.definejae234.cardproject.card.entity.Card;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Transactional      // 자동 롤백
class CardServiceTest {

    @Value("${file.path}")
    private String upload;

    @Autowired
    CardService cardService;

    @Test
    void cardListInfo() {
        List<CardDto> cardListInfo = cardService.cardListInfo();
        System.out.println(cardListInfo);
        Assertions.assertNotNull(cardListInfo);
//        Assertions.assertEquals(1, cardListInfo.size());
    }

    @Test
    void cardInsertInfo() {
        CardDto cardDto = CardDto.builder()
                .name("테스트 카드2")
                .cate(CardCateEnum.Check.name())
                .annual(10)
                .pre(30)
                .corp(CardCorpEnum.Kookmin.getName())
                .build();
        int result = cardService.cardInsertInfo(cardDto);
        Assertions.assertEquals(1, result);
    }


    @Test
    void test01() {
        int result = 100 + 10;
        assertThat(result)
                .isPositive()
                .isEqualTo(110)
                .isGreaterThan(0);
    }

    @Test
    void cardFindById() {
        CardDto cardDto = cardService.cardFindById(1);
        assertThat(cardDto).isNotNull();
    }

    @Test
    void cardUpdateInfo() {
        CardDto cardDto = cardService.cardFindById(4);
        cardDto.setName("(수정) 제목");
        int result = cardService.cardUpdateInfo(cardDto);
        assertThat(result).isPositive();
    }

    @Test
    void cardBrandMerge() {
        CardBrandDto cardBrandDto = CardBrandDto.builder()
                .id(26)
                .visa(true)
                .master(true)
                .build();
        int result = cardService.cardBrandMerge(cardBrandDto);
        assertThat(result).isEqualTo(1);
    }

    @Test
    void cardBenefitFindById() {
        CardBenefitDto cardBenefitDto = cardService.cardBenefitFindById(2);
        assertThat(cardBenefitDto).isNotNull();
    }

    @Test
    void cardBenefitMerge() {
        CardBenefitDto cardBenefitDto = CardBenefitDto.builder()
                .id(26)
                .fuel(false)
                .shop(true)
                .comm(false)
                .food(true)
                .cafe(false)
                .build();
        int result = cardService.cardBenefitMerge(cardBenefitDto);
        assertThat(result).isEqualTo(1);
    }

    @Test
    void findIdbyCardName() {
        String name = "Easy All 티타늄카드";
        int result = cardService.findIdByCardName(name);
        System.out.println("result:" + result); // id 값
        assertThat(result).isGreaterThan(0); // 존재 여부만 확인
    }

    @Test
    void deleteCardById() {
        cardService.deleteCardBenefitById(4);

        CardBenefitDto deletedCardBF = cardService.cardBenefitFindById(4);
        assertThat(deletedCardBF).isNull();
//        CardBrandDto deletedCardB = cardService.cardBrandFindById(13);
//        assertThat(deletedCardB).isNull();
//        CardDto deletedCard = cardService.cardFindById(13);
//        assertThat(deletedCard).isNull();
    }

    @Test
    void cardListInfoByBrand() {
        CardConditionDto cardConditionDto = CardConditionDto.builder()
                .cardFindBrand("카페")
                .build();

        List<CardDto> cardListInfo = cardService.cardListInfoByBrand(cardConditionDto);
        System.out.println("cardListInfo.size() : " + cardListInfo.size());
        Assertions.assertNotNull(cardListInfo);
    }

    @Test
    void firstPageFindCard() {
        CardFirstFindPageDto cardFirstFindPageDto = CardFirstFindPageDto.builder()
                .findBenefitName(List.of("주유"))
                .findBenefitNum(1)
                .findCateName("CRD")
                .isShareState(true)
                .build();
        List<CardDto> cardDtoList = cardService.cardListFirstPageFind(cardFirstFindPageDto);
        System.out.println("cardDtoList.size() : " + cardDtoList.size());
        Assertions.assertNotNull(cardDtoList);
    }

    @Test
    void clearSecondResultTable() {
        int result = cardService.clearSecondResultTable();
        System.out.println("result:" + result);
//        Assertions.assertEquals(1, result);
    }

    @Test
    void inputSecondResultTable() {
        CardFirstFindPageDto cardFirstFindPageDto = CardFirstFindPageDto.builder()
                .findBenefitName(List.of("주유"))
                .findBenefitNum(1)
                .findCateName("CRD")
                .build();
        int result = cardService.inputSecondResultTable(cardFirstFindPageDto);
        System.out.println("result:" + result);
    }

    @Test
    void cardListSecondPage() {
        List<CardDto> cardDtoList =  cardService.cardListSecondPage();
        System.out.println("cardDtoList.size() : " + cardDtoList.size());
    }

    // csv scrap test 목록
    @Test
    void csvTotalPage() {
        List<CardDto> cardDtoList = cardService.csvTotalPage();
        System.out.println("cardDtoList.size() : " + cardDtoList.size());;
    }

    // csv scrap image save 까지 테스트
    @Test
    void insertCsvCardTableData() throws IOException {

        // 테스트 이미지로 생성
        MultipartFile mockFile = new MockMultipartFile(
                "file",                      // 파라미터 이름
                "test-image.jpg",            // 파일 이름
                "image/jpeg",                // MIME 타입
                "dummy image content".getBytes() // 파일 내용
        );

        CardDto cardDto = CardDto.builder()
                .name("test2")
                .cate("CRD")
                .corp("국민카드")
                .annual(10)
                .pre(30)
                .cardImage(mockFile)
                .build();

        // 저장 경로 설정
        String folderPath = upload + "/cardImage/";
        File directory = new File(folderPath);

        // 폴더가 없으면 생성
        if (!directory.exists()) {
            directory.mkdirs(); // 상위 폴더까지 모두 생성
        }

        // 추가된 DTO 필드의 이름을 DB column에 추가
        if (!mockFile.isEmpty()) {
            String originalName = mockFile.getOriginalFilename();
            mockFile.transferTo(new File(folderPath + originalName));
            cardDto.setCardImagePath(originalName); // DTO에 파일명 저장용 필드 (cardImagePath)
        }

        if (!mockFile.isEmpty()) {
            String renamedName = UUID.randomUUID() + "_" + mockFile.getOriginalFilename();
            mockFile.transferTo(new File(folderPath + renamedName));
            cardDto.setCardRenameImagePath(renamedName); // DTO에 파일명 저장용 필드 (cardRenameImagePath)
        }

        int result = cardService.insertCsvCardTableData(cardDto);
        System.out.println("result:" + result);
    }

    @Test
    void findIdByCsvCardData() {
        CardDto cardDto = cardService.findIdByCsvCardData(2389);
        System.out.println("cardDto.getName() : " + cardDto.getName());
    }


    @Test
    void updateCsvCardTableData() {
        CardDto cardDto = cardService.findIdByCsvCardData(2389);
        cardDto.setName("mtest2");
        cardDto.setCate(cardDto.getCate());
        cardDto.setCorp(cardDto.getCorp());
        cardDto.setAnnual(cardDto.getAnnual());
        cardDto.setDiscontinue(false);
        cardDto.setSharestate(true);
        int result = cardService.updateCsvCardTableData(cardDto);
        System.out.println("result:" + result);
    }

    @Test
    void findIdByCsvCardBrandData() {
        CardBrandDto cardBrandDto = cardService.findIdByCsvCardBrandData(2385);
        System.out.println(cardBrandDto);
    }

    @Test
    void mergeCsvCardBrandWithCardTable() {
        CardBrandDto cardBrandDto = CardBrandDto.builder()
                .id(2389)
                .amex(true)
                .build();
        int result = cardService.mergeCsvCardBrandWithCardTable(cardBrandDto);
        System.out.println("result:" + result);
    }


    @Test
    void findIdByCsvCardBenefitData() {
        CardBenefitDto cardBenefitDto = cardService.findIdByCsvCardBenefitData(2385);
        System.out.println(cardBenefitDto);
    }


    @Test
    void mergeCsvCardBenefitWithCardTable() {
        CardBenefitDto cardBenefitDto = CardBenefitDto.builder()
                .id(2389)
                .fuel(true)
                .build();
        int result = cardService.mergeCsvCardBenefitWithCardTable(cardBenefitDto);
        System.out.println("result:" + result);
    }

    @Test
    void deleteCsvCardDataWithID() {
        int result = cardService.deleteCsvCardDataWithID(2389);
        System.out.println("result:" + result);
    }

    @Test
    void deleteCsvCardBrandDataWithID() {
        int result = cardService.deleteCsvCardBrandDataWithID(2389);
        System.out.println("result:" + result);
    }

    @Test
    void deleteCsvCardBenefitDataWithID() {
        int result = cardService.deleteCsvCardBenefitDataWithID(2389);
        System.out.println("result:" + result);
    }

    @Test
    void cardCsvFirstPageSelectionList() {
        CardFirstFindPageDto cardFirstFindPageDto = CardFirstFindPageDto.builder()
                .findBenefitName(List.of("쇼핑", "카페"))
                .findBenefitNum(2)
                .findCateName("CRD")
                .build();
        List<CardDto> cardDtoList = cardService.cardCsvFirstPageSelectionList(cardFirstFindPageDto);
        System.out.println("cardDtoList.size() : " + cardDtoList.size());
    }

    @Test
    void cardListNormalAll() {
        List<CardDto> cardDtoList = cardService.cardListNormalAll();
        System.out.println("cardDtoList.size() : " + cardDtoList.size());
    }

    @Test
    void cardDataNormalInfoById() {
        CardNormalInfoDto cardNormalInfoDto = cardService.cardDataNormalInfoById(23);
        System.out.println("cardNormalInfoDto.getName() : " + cardNormalInfoDto.getName());
    }
}