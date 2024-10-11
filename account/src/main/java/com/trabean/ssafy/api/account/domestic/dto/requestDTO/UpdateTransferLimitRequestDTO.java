package com.trabean.ssafy.api.account.domestic.dto.requestDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.RequestHeader;
import lombok.Builder;
import lombok.Getter;

/**
 * SSAFY 금융 API p.59 - 계좌 이체 한도 변경 requestDTO
 */
@Builder
@Getter
public class UpdateTransferLimitRequestDTO {

    @JsonProperty("Header")
    private RequestHeader header;

    @JsonProperty("accountNo")
    private String accountNo;

    @JsonProperty("oneTimeTransferLimit")
    private Long oneTimeTransferLimit;

    @JsonProperty("dailyTransferLimit")
    private Long dailyTransferLimit;
}
