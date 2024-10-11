package com.trabean.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePaymentInfoRequest {
    private Long userId;
    private Long accountId; // 돈 빠져나가는 계좌
    private Long merchantId;
    private Double foreignAmount;
}
