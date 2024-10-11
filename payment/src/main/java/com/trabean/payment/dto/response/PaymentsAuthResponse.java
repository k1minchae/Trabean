package com.trabean.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentsAuthResponse {
    private String transactionId;
    private Long payId;
    private String paymentDate;
}
