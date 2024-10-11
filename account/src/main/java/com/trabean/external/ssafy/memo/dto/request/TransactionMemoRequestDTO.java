package com.trabean.external.ssafy.memo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.RequestHeader;
import lombok.Builder;
import lombok.Getter;

/**
 * SSAFY 금융 API p.266 - 거래내역 메모 requestDTO
 */
@Builder
@Getter
public class TransactionMemoRequestDTO {

    @JsonProperty("Header")
    private RequestHeader header;

    @JsonProperty("accountNo")
    private String accountNo;

    @JsonProperty("transactionUniqueNo")
    private Long transactionUniqueNo;

    @JsonProperty("transactionMemo")
    private String transactionMemo;
}
