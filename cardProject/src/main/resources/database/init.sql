-- card 테이블
CREATE TABLE card
(
    id          NUMBER
        CONSTRAINT card_id_pk PRIMARY KEY,
    name        varchar2(100) CONSTRAINT card_name_nn_uniq NOT NULL UNIQUE,
    cate        varchar2(100) CONSTRAINT card_cate_nn NOT NULL,
    annual      NUMBER
        CONSTRAINT card_annual_nn NOT NULL,
    pre         NUMBER
        CONSTRAINT card_pre_nn NOT NULL,
    corp        varchar2(100) CONSTRAINT card_corp_nn NOT NULL,
    discontinue NUMBER(1) DEFAULT 0, -- 단종여부(1이면 단종)
    sharestate  NUMBER(1) DEFAULT 0  -- 조회가능상태(1이면 조회가능)
);

-- card 시퀀스
CREATE SEQUENCE CARD_SEQ
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 9999999999
    MINVALUE 1
    NOCYCLE;

-- 카드 데이터 insert
INSERT INTO card (id, name, cate, annual, pre, corp)
VALUES (card_seq.nextval, 'Easy All 티타늄카드', 'CRD', 10, 30, '국민카드');

-- brand 테이블
CREATE TABLE brand
(
    id     NUMBER
        CONSTRAINT brand_card_id PRIMARY KEY,
    visa   NUMBER(1) DEFAULT 0, -- visa
    master NUMBER(1) DEFAULT 0, -- master
    bc     NUMBER(1) DEFAULT 0, -- bc
    amex   NUMBER(1) DEFAULT 0, -- amex
    FOREIGN KEY (id) REFERENCES card (id)
);


-- benefit 테이블
CREATE TABLE benefit
(
    id   NUMBER
        CONSTRAINT benefit_card_id PRIMARY KEY,
    fuel NUMBER(1) DEFAULT 0, -- 주유
    comm NUMBER(1) DEFAULT 0, -- 통신
    shop NUMBER(1) DEFAULT 0, -- 쇼핑
    food NUMBER(1) DEFAULT 0, -- 푸드
    cafe NUMBER(1) DEFAULT 0, -- 카페
    FOREIGN KEY (id) REFERENCES card (id)
);

-- 카드 결제 브랜드 정보 머지 기능 추가 (insert, update를 동시에)
MERGE INTO brand b
    USING (
        SELECT 4 AS id, 1 AS visa, 1 AS master, 1 AS bc, 1 AS amex FROM dual
    ) src
    ON (b.id = src.id)
    WHEN MATCHED THEN
        UPDATE SET
            b.visa = src.visa,
            b.master = src.master,
            b.bc = src.bc,
            b.amex = src.amex
    WHEN NOT MATCHED THEN
        INSERT (id, visa, master, bc, amex)
            VALUES (src.id, src.visa, src.master, src.bc, src.amex);

-- 카드 혜택 정보 머지 기능 추가 (insert, update를 동시에)
MERGE INTO benefit bf
    USING (
        SELECT 2 AS id, 1 AS fuel, 1 AS comm, 1 AS shop, 1 AS food, 1 AS cafe FROM dual
    ) src
    ON (bf.id = src.id)
    WHEN MATCHED THEN
        UPDATE SET
            bf.fuel = src.fuel,
            bf.comm = src.comm,
            bf.shop = src.shop,
            bf.food = src.food,
            bf.cafe = src.cafe
    WHEN NOT MATCHED THEN
        INSERT (id, fuel, comm, shop, food, cafe)
            VALUES (src.id, src.fuel, src.comm, src.shop, src.food, src.cafe);