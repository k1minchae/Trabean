package com.trabean.travel.callApi.client;

import com.trabean.travel.callApi.dto.request.AccountBalanceApiRequestDto;
import com.trabean.travel.callApi.dto.request.AccountHistoryApiRequestDto;
import com.trabean.travel.callApi.dto.request.AccountTransferApiRequestDto;
import com.trabean.travel.callApi.dto.response.AccountBalanceApiResponseDto;
import com.trabean.travel.callApi.dto.response.AccountHistoryApiResponseDto;
import com.trabean.travel.callApi.dto.response.AccountTransferApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "demandDepositClient", url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit", configuration = FeignClientsConfiguration.class)
public interface DemandDepositClient {

    /**
     * SSAFY API : 원화통장 계좌번호로 계좌 잔액조회
     */
    @PostMapping("/inquireDemandDepositAccountBalance")
    AccountBalanceApiResponseDto getKrwAccountBalance(
            @RequestBody AccountBalanceApiRequestDto accountBalanceApiRequestDto);

    /**
     * SSAFY API : 계좌 이체
     */
    @PostMapping("/updateDemandDepositAccountTransfer")
    AccountTransferApiResponseDto transferAccount(
            @RequestBody AccountTransferApiRequestDto accountTransferApiRequestDto);

    /**
     * SSAFY API : 원화통장 계좌 거래 내역조회
     */
    @PostMapping("/inquireTransactionHistoryList")
    AccountHistoryApiResponseDto getKrwAccountHistoryList(
            @RequestBody AccountHistoryApiRequestDto accountHistoryApiRequestDto);

}
