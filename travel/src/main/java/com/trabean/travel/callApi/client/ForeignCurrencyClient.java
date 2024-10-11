package com.trabean.travel.callApi.client;

import com.trabean.travel.callApi.dto.request.GetAccountBalanceRequestDto;
import com.trabean.travel.callApi.dto.response.GetAccountBalanceResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "foreignCurrencyClient", url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/foreignCurrency", configuration = FeignClient.class)
public interface ForeignCurrencyClient {

    /**
     * 계좌번호로 계좌 조회 (외환)
     */
    @PostMapping("/inquireForeignCurrencyDemandDepositAccountBalance")
    GetAccountBalanceResponseDto getForeignAccountBalance(
            @RequestBody GetAccountBalanceRequestDto getAccountBalanceRequestDto);

}
