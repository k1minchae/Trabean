package com.trabean.external.ssafy.verification.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.RequestHeader;
import lombok.Builder;
import lombok.Getter;

import static com.trabean.constant.Constant.APPLICATION_NAME;

/**
 * SSAFY 금융 API p.202 - 1원 송금 requestDTO
 */
@Builder
@Getter
public class OpenAccountAuthRequestDTO {

    @JsonProperty("Header")
    private RequestHeader header;

    @JsonProperty("accountNo")
    private String accountNo;

    @JsonProperty("authText")
    private final String authText = APPLICATION_NAME;
}
