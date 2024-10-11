package com.trabean.travel.callApi.client;

import com.trabean.travel.callApi.dto.request.GetAccountNumberRequestDto;
import com.trabean.travel.callApi.dto.response.GetAccountNumberResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "accountClient", url = "http://j11a604.p.ssafy.io:8081/api/accounts", configuration = FeignClientsConfiguration.class)
public interface AccountClient {

    /**
     * accountId로 계좌번호 조회
     */
    @PostMapping("/get-account-number")
    GetAccountNumberResponseDto getAccount(@RequestBody GetAccountNumberRequestDto getAccountNumberRequestDto);

}