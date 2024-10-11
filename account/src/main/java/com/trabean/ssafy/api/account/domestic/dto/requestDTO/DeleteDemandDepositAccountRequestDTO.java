package com.trabean.ssafy.api.account.domestic.dto.requestDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.RequestHeader;
import lombok.Builder;
import lombok.Getter;

/**
 * SSAFY 금융 API p.69 - 계좌 해지 requestDTO
 */
@Builder
@Getter
public class DeleteDemandDepositAccountRequestDTO {

    @JsonProperty("Header")
    private RequestHeader header;

    @JsonProperty("accountNo")
    private String accountNo;

    @JsonProperty("refundAccountNo")
    private String refundAccountNo;
}
