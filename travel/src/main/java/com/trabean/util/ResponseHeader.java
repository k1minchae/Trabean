package com.trabean.util;

import lombok.Getter;

/**
 * {@code ResponseHeader} 클래스는 SSAFY 금융 API 응답 헤더 정보를 담는 클래스입니다.
 *
 * <p>이 클래스는 Lombok의 {@code @Getter} 어노테이션을 사용하여 모든 필드에 대한 getter 메서드를 자동으로 생성합니다.
 * 따라서 responseCode, responseMessage 등 각 필드 값은 getter 메서드를 통해 접근할 수 있습니다.</p>
 *
 * @author FickleBoBo
 * @since 2024-09-21
 */
@Getter
public class ResponseHeader {
    private String responseCode;
    private String responseMessage;
    private String apiName;
    private String transmissionDate;
    private String transmissionTime;
    private String institutionCode;
    private String fintechAppNo;
    private String apiServiceCode;
    private String institutionTransactionUniqueNo;
}
