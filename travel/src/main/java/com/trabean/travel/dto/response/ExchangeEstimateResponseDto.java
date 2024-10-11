package com.trabean.travel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExchangeEstimateResponseDto {

    private CurrencyDetail currency;
    private CurrencyDetail exchangeCurrency;

    @Getter
    @AllArgsConstructor
    public static class CurrencyDetail {
        private String amount;
        private String country;
        private String currency;
    }

}
