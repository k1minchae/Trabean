package com.trabean.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceResponse {
    @JsonProperty("Header")
    private Header header;

    @JsonProperty("REC")
    private BalanceResponse.REC rec;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class REC {
        private String bankCode;
        private String accountNo;
        private Long accountBalance;
        private String accountCreatedDate;
        private String accountExpiryDate;
        private String lastTransactionDate;
        private String currency;
    }
}
