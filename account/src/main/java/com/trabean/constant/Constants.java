package com.trabean.constant;

public class Constants {
    public static final String APPLICATION_NAME = "Trabean";
    public static final String INSTITUTION_CODE = "00100";
    public static final String FINTECH_APP_NO = "001";
    public static final String API_KEY = System.getenv("API_KEY");

    /**
     * 개인 통장 상품 고유번호
     */
    public static final String PERSONAL_ACCOUNT_TYPE_UNIQUE_NO = "001-1-45f35a47cc6649";

    /**
     * 한화 여행통장 상품 고유번호
     */
    public static final String DOMESTIC_TRAVEL_ACCOUNT_TYPE_UNIQUE_NO = "001-1-2e58332128994c";

    /**
     * 통장 비밀번호 해싱용 pepper
     * SHA-256 (Trabean)
     */
    public static final String PEPPER = "ac052c71c986b28977452cb7ffa50f78725c7f5a52264d639561866f31edf26e";
}
