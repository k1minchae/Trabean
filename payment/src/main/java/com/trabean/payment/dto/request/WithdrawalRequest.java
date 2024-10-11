package com.trabean.payment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalRequest {

    @JsonProperty("Header")
    private Header header;

    @JsonProperty("accountNo")
    private String accountNo;

    @JsonProperty("transactionBalance")
    private Long transactionBalance;

    @JsonProperty("transactionSummary")
    private String transactionSummary;
}
