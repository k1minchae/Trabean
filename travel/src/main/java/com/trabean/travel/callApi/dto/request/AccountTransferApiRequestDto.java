package com.trabean.travel.callApi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.RequestHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountTransferApiRequestDto {

    @JsonProperty("Header")
    private RequestHeader header;

    private String depositAccountNo;
    private String depositTransactionSummary;
    private Long transactionBalance;
    private String withdrawalAccountNo;
    private String withdrawalTransactionSummary;

}
