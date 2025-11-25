package com.definejae234.cardproject.card.dao;

import com.definejae234.cardproject.card.dto.*;
import com.definejae234.cardproject.card.entity.Card;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface CardDao {  // mapper/card.xml id로 연동할 함수 나열
    int cardInsertInfo(CardDto cardDto);
    List<CardDto> cardListInfo();
    CardDto cardFindById(int id);
    int cardUpdateInfo(CardDto cardDto);
    CardBrandDto cardBrandFindById(int id);
    int cardBrandMerge(CardBrandDto cardBrandDto);
    CardBenefitDto cardBenefitFindById(int id);
    int cardBenefitMerge(CardBenefitDto cardBenefitDto);

    // @Param을 붙인 이유는 mapper의 파라미터 이름을 표시하기 위함
    int findIdByCardName(@Param("name") String name);
    int deleteCardById(@Param("id") int id);
    int deleteCardBenefitById(@Param("id") int id);
    int deleteCardBrandById(@Param("id") int id);

    List<CardDto> cardListInfoByBrand(CardConditionDto cardConditionDto);
    List<CardDto> cardListFirstPageFind(CardFirstFindPageDto cardFirstFindPageDto);

    // 카드 조회 첫번째 페이지에서 찾은 두번째 데이터 테이블 리스트
    int clearSecondResultTable();
    int inputSecondResultTable(CardFirstFindPageDto cardFirstFindPageDto);
    List<CardDto> cardListSecondPage();

    //카드 조회 첫번째 페이지에서 필터를 적용하여 출력하는 리스트
    List<CardDto> cardListSecondPage(CardFilterRequestDto filterDto);

    // csv 카드 스크랩 데이터 DB 쿼리
    List<CardDto> csvTotalPage();             //  csv 전체 페이지
    List<CardDto> csvNormalTotalPage();
    int insertCsvCardTableData(CardDto cardDto);       // csv 데이터 입력
    CardBrandDto findIdByCsvCardBrandData(int id);
    int mergeCsvCardBrandWithCardTable(CardBrandDto cardBrandDto); // Brand 항목 Merge
    CardBenefitDto findIdByCsvCardBenefitData(int id); // csv 테이블 내에 id를 통해 카드데이터 찾기
    int mergeCsvCardBenefitWithCardTable(CardBenefitDto cardBenefitDto); // Brand 항목 Merge
    CardDto findIdByCsvCardData(int id);            // csv 테이블 내에 id를 통해 카드데이터 찾기
    int updateCsvCardTableData(CardDto cardDto);    // csv 테이블 Card데이터 업데이트
    // csv 테이블로 만든 카드 데이터 삭제
    int deleteCsvCardDataWithID(int id);
    int deleteCsvCardBrandDataWithID(int id);
    int deleteCsvCardBenefitDataWithID(int id);
    List<CardDto> cardCsvFirstPageSelectionList(CardFirstFindPageDto cardFirstFindPageDto);  // 카드 페이지 첫번쨰 조건 리스트
    List<CardDto> cardCsvNormalFirstPageSelectionList(CardFirstFindPageDto cardFirstFindPageDto);  // 카드 페이지 첫번쨰 조건 리스트
    CardNormalInfoDto cardCsvDataNormalInfoById(int id);
    int findIdByCsvCardName(String name);
    int inputCsvSecondResultTable(CardFirstFindPageDto cardFirstFindPageDto);
    int inputCsvNormalSecondResultTable(CardFirstFindPageDto cardFirstFindPageDto);
    // 함수명이 같으면 같은 xml을 사용할 수 있음(처음암.. ㄷㄷ)
    List<CardDto> cardCsvListSecondPage();
    //카드 조회 첫번째 페이지에서 필터를 적용하여 출력하는 리스트
    List<CardDto> cardCsvListSecondPage(CardFilterRequestDto filterDto);
    // 일반 회원 전용 카드 리스트
    List<CardDto> cardListNormalAll();
    // 일반 회원 전용 카드 상세 페이지
    CardNormalInfoDto cardDataNormalInfoById(int id);
    // 일반 회원 전용 카드 조회 1번째 페이지
    List<CardDto> cardNormalListFirstPageFind(CardFirstFindPageDto cardFirstFindPageDto);
    // 일반 회원 전용 카드 조회 2번째 테이블 저장
    int inputSecondResultNormalTable(CardFirstFindPageDto cardFirstFindPageDto);

    List<CardDto> findAll(UnifiedPageAndCardFilterRequestDto unifiedPageAndCardFilterRequestDto);
    int totalCard(UnifiedPageAndCardFilterRequestDto unifiedPageAndCardFilterRequestDto);

    int clearThirdResultTable();
    int copyFilteredResultsToThird(UnifiedPageAndCardFilterRequestDto unifiedPageAndCardFilterRequestDto);
    List<CardDto> findNewCardList(int size);

    // 중복된 카드 이름이 있는지 확인
    boolean existsByName(String name);
}
