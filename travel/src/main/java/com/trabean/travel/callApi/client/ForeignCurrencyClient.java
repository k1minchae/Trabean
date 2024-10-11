package com.trabean.travel.callApi.client;

import com.trabean.travel.callApi.dto.request.AccountBalanceApiRequestDto;
import com.trabean.travel.callApi.dto.request.AccountHistoryApiRequestDto;
import com.trabean.travel.callApi.dto.request.DepositForeignAccountApiRequestDto;
import com.trabean.travel.callApi.dto.response.AccountBalanceApiResponseDto;
import com.trabean.travel.callApi.dto.response.AccountHistoryApiResponseDto;
import com.trabean.travel.callApi.dto.response.DepositForeignAccountApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "foreignCurrencyClient", url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/foreignCurrency", configuration = FeignClientsConfiguration.class)
public interface ForeignCurrencyClient {

    /**
     * SSAFY API : 계좌번호로 외화 계좌 잔액 조회
     */
    @PostMapping("/inquireForeignCurrencyDemandDepositAccountBalance")
    AccountBalanceApiResponseDto getForeignAccountBalance(
            @RequestBody AccountBalanceApiRequestDto getAccountBalanceRequestDto);

    /**
     * SSAFY API : 외화 계좌 거래 내역 조회
     */
    @PostMapping("/inquireForeignCurrencyTransactionHistoryList")
    AccountHistoryApiResponseDto getForeignAccountHistoryList(
            @RequestBody AccountHistoryApiRequestDto accountHistoryApiRequestDto);

    /**
     * SSAFY API : 외화 계좌 입금
     */
    @PostMapping("/updateForeignCurrencyDemandDepositAccountDeposit")
    DepositForeignAccountApiResponseDto updateForeignAccountDeposit(
            @RequestBody DepositForeignAccountApiRequestDto depositForeignAccountApiRequestDto);

}
