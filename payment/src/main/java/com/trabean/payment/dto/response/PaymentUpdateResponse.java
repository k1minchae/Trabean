package com.trabean.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUpdateResponse {
    private String status;
    private String message;
    private PaymentData data;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentData {
        private Long payId;
        private Long accountId;
        private Double foreignAmount;
        private Long krwAmount;
        private String merchantName;
        private String transactionId;
    }
}