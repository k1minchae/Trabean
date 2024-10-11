package com.trabean.travel.callApi.client;

import com.trabean.travel.callApi.dto.response.ExchangeRateOpenApiResponseDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "koreaeximClient", url = "https://www.koreaexim.go.kr/site/program/financial")
public interface KoreaeximClient {

    /**
     * 한국수출입은행 Open API : 현재 환율 API
     */
    @GetMapping("/exchangeJSON")
    List<ExchangeRateOpenApiResponseDto> getExchangeRateFromOpenApi(@RequestParam String authkey,
                                                                    @RequestParam String searchdate,
                                                                    @RequestParam String data);

}
