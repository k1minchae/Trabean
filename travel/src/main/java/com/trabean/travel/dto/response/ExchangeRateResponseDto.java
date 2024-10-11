package com.trabean.travel.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExchangeRateResponseDto {

    private Long id;
    private String country;
    private String currency;
    private Double exchangeRate;
    private Double pastExchangeRate;
    private Double changeRate;

    @Builder
    public ExchangeRateResponseDto(Long id, String country, String currency, Double exchangeRate,
                                   Double pastExchangeRate, Double changeRate) {
        this.id = id;
        this.country = country;
        this.currency = currency;
        this.exchangeRate = exchangeRate;
        this.pastExchangeRate = pastExchangeRate;
        this.changeRate = changeRate;
    }

}
