package com.trabean.external.ssafy.domestic.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.RequestHeader;
import lombok.Builder;
import lombok.Getter;

/**
 * SSAFY 금융 API p.56 - 계좌 이체 requestDTO
 */
@Builder
@Getter
public class UpdateDemandDepositAccountTransferRequestDTO {

    @JsonProperty("Header")
    private RequestHeader header;

    @JsonProperty("depositAccountNo")
    private String depositAccountNo;

    @JsonProperty("transactionBalance")
    private Long transactionBalance;

    @JsonProperty("withdrawalAccountNo")
    private String withdrawalAccountNo;

    @JsonProperty("depositTransactionSummary")
    private String depositTransactionSummary;

    @JsonProperty("withdrawalTransactionSummary")
    private String withdrawalTransactionSummary;
}
