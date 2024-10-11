package com.trabean.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestPaymentRequest {
    private String transactionId;
    private Long userId;
    private Long payId;
    private Long merchantId;
    private Long krwAmount;
    private Double foreignAmount;
}
