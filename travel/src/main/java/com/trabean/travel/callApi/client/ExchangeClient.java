package com.trabean.travel.callApi.client;

import com.trabean.travel.callApi.dto.request.ExchangeApiRequestDto;
import com.trabean.travel.callApi.dto.request.ExchangeEstimateApiRequestDto;
import com.trabean.travel.callApi.dto.response.ExchangeApiResponseDto;
import com.trabean.travel.callApi.dto.response.ExchangeEstimateApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "exchangeClient", url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/exchange", configuration = FeignClientsConfiguration.class)
public interface ExchangeClient {

    /**
     * SSAFY API : 환전 예상 금액 조회
     */
    @PostMapping("/estimate")
    ExchangeEstimateApiResponseDto getExchangeEstimate(
            @RequestBody ExchangeEstimateApiRequestDto exchangeEstimateApiRequestDto);

    /**
     * SSAFY API : 환전 신청
     */
    @PostMapping
    ExchangeApiResponseDto exchange(@RequestBody ExchangeApiRequestDto exchangeApiRequestDto);

}
