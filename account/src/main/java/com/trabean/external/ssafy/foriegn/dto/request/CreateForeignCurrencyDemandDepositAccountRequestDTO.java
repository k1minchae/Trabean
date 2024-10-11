package com.trabean.external.ssafy.foriegn.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.RequestHeader;
import lombok.Builder;
import lombok.Getter;

/**
 * SSAFY 금융 API p.228 - 외화 계좌 생성 requestDTO
 */
@Builder
@Getter
public class CreateForeignCurrencyDemandDepositAccountRequestDTO {

    @JsonProperty("Header")
    private RequestHeader header;

    @JsonProperty("accountTypeUniqueNo")
    private String accountTypeUniqueNo;

    @JsonProperty("currency")
    private String currency;
}
