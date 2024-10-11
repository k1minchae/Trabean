package com.trabean.util;

import java.security.SecureRandom;
import lombok.Builder;
import lombok.Getter;

/**
 * {@code RequestHeader} 클래스는 SSAFY 금융 API 요청 헤더 정보를 구성하는 클래스입니다. 이 클래스는 빌더 패턴을 사용하여 객체를 생성하며, apiName과 userKey를 제외한 필드를
 * 자동으로 설정합니다. 이 클래스는 불변 클래스이며, 스레드 안전성을 보장합니다.
 *
 * <p>사용 예시:</p>
 * <pre>
 *     RequestHeader header = RequestHeader.builder()
 *         .apiName("API 이름")
 *         .userKey("유저 Key")
 *         .build();
 * </pre>
 *
 * @author FickleBoBo
 * @since 2024-09-21
 */
@Builder
@Getter
public class RequestHeader {

    private static final String INSTITUTION_CODE = "00100";
    private static final String FINTECH_APP_NO = "001";
    private static final String API_KEY = System.getenv("API_KEY");

    private final String apiName;
    private final String transmissionDate;
    private final String transmissionTime;
    private final String institutionCode;
    private final String fintechAppNo;
    private final String apiServiceCode;
    private final String institutionTransactionUniqueNo;
    private final String apiKey;
    private final String userKey;

    /**
     * {@code RequestHeaderBuilder} 내부 클래스는 {@link RequestHeader} 객체를 빌더 패턴으로 생성합니다.
     */
    public static class RequestHeaderBuilder {

        /**
         * API 이름을 설정하고, userKey를 제외한 나머지 필드를 자동으로 설정합니다.
         *
         * @param apiName API 이름
         * @return {@code RequestHeaderBuilder} 객체
         */
        public RequestHeaderBuilder apiName(String apiName) {
            CurrentDateTime currentDateTime = CurrentDateTime.getCurrentDateTime();

            this.apiName = apiName;
            this.transmissionDate = currentDateTime.getYYYYMMDD();
            this.transmissionTime = currentDateTime.getHHMMSS();
            this.institutionCode = INSTITUTION_CODE;
            this.fintechAppNo = FINTECH_APP_NO;
            this.apiServiceCode = apiName;
            this.institutionTransactionUniqueNo = createInstitutionTransactionUniqueNo(
                    currentDateTime.getYYYYMMDDHHMMSS());
            this.apiKey = API_KEY;
            return this;
        }
    }

    /**
     * 기관거래고유번호를 생성합니다. 거래 번호는 날짜와 시간 (YYYYMMDDHHMMSS)과 난수 6자리로 구성됩니다.
     *
     * @param YYYYMMDDHHMMSS 날짜 및 시간 정보
     * @return 생성된 기관거래고유번호 (낮은 확률로 중복될 수 있음)
     */
    private static String createInstitutionTransactionUniqueNo(String YYYYMMDDHHMMSS) {
        return RandomStringGenerator.generateRandomString(YYYYMMDDHHMMSS);
    }

    /**
     * {@code RandomStringGenerator}는 난수 문자열을 생성하는 클래스입니다.
     */
    private static class RandomStringGenerator {

        /**
         * 난수를 생성할 때 사용할 {@link SecureRandom} 객체.
         */
        private static final SecureRandom SECURE_RANDOM = new SecureRandom();
        private static final String NUMBERS = "0123456789";

        /**
         * 입력된 날짜 및 시간(YYYYMMDDHHMMSS)을 기반으로 20자리 숫자 난수를 생성하여 반환합니다.
         *
         * @param YYYYMMDDHHMMSS 날짜 및 시간 정보
         * @return 생성된 난수 문자열 (YYYYMMDDHHMMSS + 6자리 난수)
         */
        public static String generateRandomString(String YYYYMMDDHHMMSS) {
            StringBuilder sb = new StringBuilder(20);

            sb.append(YYYYMMDDHHMMSS);

            for (int i = 0; i < 6; i++) {
                int index = SECURE_RANDOM.nextInt(NUMBERS.length());
                sb.append(NUMBERS.charAt(index));
            }

            return sb.toString();
        }
    }
}
