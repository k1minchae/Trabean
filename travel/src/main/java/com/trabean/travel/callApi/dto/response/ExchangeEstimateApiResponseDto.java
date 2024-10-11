package com.trabean.travel.callApi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.ResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExchangeEstimateApiResponseDto {

    @JsonProperty("Header")
    private ResponseHeader header;

    @JsonProperty("REC")
    private REC rec;

    @Getter
    public static class REC {
        private ApiCurrencyDetail currency;
        private ApiCurrencyDetail exchangeCurrency;
    }

    @Getter
    public static class ApiCurrencyDetail {
        private String amount;
        private String currency;
        private String currencyName;
    }

}
