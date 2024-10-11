package com.trabean.travel.callApi.client;

import com.trabean.travel.callApi.dto.request.ExchangeRateApiRequestDto;
import com.trabean.travel.callApi.dto.response.ExchangeRateApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "exchangeRateClient", url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/exchangeRate", configuration = FeignClientsConfiguration.class)
public interface ExchangeRateClient {

    /**
     * SSAFY API : 환율 전체 조회
     */
    @PostMapping
    ExchangeRateApiResponseDto getExchangeRate(@RequestBody ExchangeRateApiRequestDto exchangeRateApiRequestDto);

}
