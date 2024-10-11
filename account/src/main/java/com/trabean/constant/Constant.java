package com.trabean.constant;

public class Constant {

    /**
     * SSAFY 금융 API 공통 Header 상수
     */
    public static final String APPLICATION_NAME = "Trabean";

    /**
     * SSAFY 금융 API 공통 Header 상수
     */
    public static final String INSTITUTION_CODE = "00100";

    /**
     * SSAFY 금융 API 공통 Header 상수
     */
    public static final String FINTECH_APP_NO = "001";

    /**
     * SSAFY 금융 API 공통 Header API KEY
     */
    public static final String API_KEY = System.getenv("API_KEY");

    /**
     * 개인 통장 상품 고유번호
     */
    public static final String PERSONAL_ACCOUNT_TYPE_UNIQUE_NO = "999-1-59afd652233549";

    /**
     * 한화 여행통장 상품 고유번호
     */
    public static final String DOMESTIC_TRAVEL_ACCOUNT_TYPE_UNIQUE_NO = "999-1-1ae0ed02c79c42";

    /**
     * 외화 여행통장 상품 고유번호
     */
    public static final String FOREIGN_TRAVEL_ACCOUNT_TYPE_UNIQUE_NO = "999-1-8ad0c6211dfa49";

    /**
     * 통장 비밀번호 해싱용 pepper
     * SHA-256 (Trabean)
     */
    public static final String PEPPER = System.getenv("PEPPER");
}
