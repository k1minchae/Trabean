package com.trabean.travel.callApi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.RequestHeader;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExchangeApiRequestDto {

    @JsonProperty("Header")
    private RequestHeader header;
    private String accountNo;
    private String exchangeCurrency;
    private String exchangeAmount;

}
