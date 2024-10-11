package com.trabean.travel.callApi.client;

import com.trabean.travel.callApi.dto.request.BankCodeApiRequestDto;
import com.trabean.travel.callApi.dto.response.BankCodeApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "bankClient", url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/bank", configuration = FeignClientsConfiguration.class)
public interface BankClient {

    /**
     * SSAFY API : 은행코드 조회
     */
    @PostMapping("/inquireBankCodes")
    BankCodeApiResponseDto getBankCode(
            @RequestBody BankCodeApiRequestDto bankCodeApiRequestDto);

}
