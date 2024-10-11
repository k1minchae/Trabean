package com.trabean.travel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExchangeRequestDto {

    private Long krwAccountId;
    private String withdrawalAccountNo;
    private String depositAccountNo;
    private String exchangeCurrency;
    private String exchangeAmount;

}
