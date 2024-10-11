package com.trabean.ssafy.api.verification.dto.requestDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.RequestHeader;
import lombok.Builder;
import lombok.Getter;

import static com.trabean.constant.Constants.APPLICATION_NAME;

/**
 * SSAFY 금융 API p.205 - 1원 송금 검증 requestDTO
 */
@Builder
@Getter
public class CheckAuthCodeRequestDTO {

    @JsonProperty("Header")
    private RequestHeader header;

    @JsonProperty("accountNo")
    private String accountNo;

    @JsonProperty("authText")
    private final String authText = APPLICATION_NAME;

    @JsonProperty("authCode")
    private String authCode;
}
