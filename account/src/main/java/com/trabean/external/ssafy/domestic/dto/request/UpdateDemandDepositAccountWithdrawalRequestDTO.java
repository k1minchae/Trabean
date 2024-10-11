package com.trabean.external.ssafy.domestic.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.RequestHeader;
import lombok.Builder;
import lombok.Getter;

/**
 * SSAFY 금융 API p.50 - 계좌 출금 requestDTO
 */
@Builder
@Getter
public class UpdateDemandDepositAccountWithdrawalRequestDTO {

    @JsonProperty("Header")
    private RequestHeader header;

    @JsonProperty("accountNo")
    private String accountNo;

    @JsonProperty("transactionBalance")
    private Long transactionBalance;

    @JsonProperty("transactionSummary")
    private String transactionSummary;
}
