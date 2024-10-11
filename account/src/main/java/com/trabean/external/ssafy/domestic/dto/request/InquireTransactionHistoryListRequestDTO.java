package com.trabean.external.ssafy.domestic.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.RequestHeader;
import lombok.Builder;
import lombok.Getter;

/**
 * SSAFY 금융 API p.62 - 계좌 거래 내역 조회 requestDTO
 */
@Builder
@Getter
public class InquireTransactionHistoryListRequestDTO {

    @JsonProperty("Header")
    private RequestHeader header;

    @JsonProperty("accountNo")
    private String accountNo;

    @JsonProperty("startDate")
    private String startDate;

    @JsonProperty("endDate")
    private String endDate;

    @JsonProperty("transactionType")
    private String transactionType;

    @JsonProperty("orderByType")
    private String orderByType;
}
