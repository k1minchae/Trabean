package com.trabean.ssafy.api.account.domestic.dto.requestDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.RequestHeader;
import lombok.Builder;
import lombok.Getter;

/**
 * SSAFY 금융 API p.35 - 계좌 생성 requestDTO
 */
@Builder
@Getter
public class CreateDemandDepositAccountRequestDTO {

    @JsonProperty("Header")
    private RequestHeader header;

    @JsonProperty("accountTypeUniqueNo")
    private String accountTypeUniqueNo;
}
