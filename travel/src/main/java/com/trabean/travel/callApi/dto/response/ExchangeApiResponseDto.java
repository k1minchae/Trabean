package com.trabean.travel.callApi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.travel.dto.response.ExchangeResponseDto.AccountInfo;
import com.trabean.travel.dto.response.ExchangeResponseDto.ExchangeCurrency;
import com.trabean.util.ResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExchangeApiResponseDto {

    @JsonProperty("Header")
    private ResponseHeader header;

    @JsonProperty("REC")
    private REC rec;

    @Getter
    public static class REC {
        private ExchangeCurrency exchangeCurrency;
        private AccountInfo accountInfo;
    }

}
