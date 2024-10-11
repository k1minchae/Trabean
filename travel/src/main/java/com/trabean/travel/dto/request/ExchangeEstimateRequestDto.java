package com.trabean.travel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExchangeEstimateRequestDto {

    private String currency;
    private String exchangeCurrency;
    private double amount;

}
