package com.trabean.travel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExchangeResponseDto {

    private ExchangeCurrency exchangeCurrency;
    private AccountInfo accountInfo;

    @Getter
    public static class ExchangeCurrency {
        private String amount;
        private String exchangeRate;
        private String currency;
        private String currencyName;
    }

    @Getter
    public static class AccountInfo {
        private String accountNo;
        private String amount;
        private String balance;
    }

}
