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