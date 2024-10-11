package com.trabean.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalResponse {
    @JsonProperty("Header")
    private Header header;

    @JsonProperty("REC")
    private WithdrawalResponse.REC rec;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class REC {
        private String transactionUniqueNo;
        private String transactionDate;
    }
}