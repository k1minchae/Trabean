package com.trabean.payment.dto.response;

import com.trabean.payment.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusResponse {
    private String transactionId;
    private String payId;
    private String passwordErrorCount;
    private PaymentStatus paymentStatus;
    private String message;
}