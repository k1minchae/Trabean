package com.trabean.payment.client.ssafy;

import com.trabean.payment.dto.request.ExchangeRateRequest;
import com.trabean.payment.dto.response.ExchangeRateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "exchangeClient", url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/exchangeRate")
public interface ExchangeClient {

    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE)
    ExchangeRateResponse getExchangeRate(@RequestBody ExchangeRateRequest request);

}
