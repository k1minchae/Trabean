package com.trabean.travel.service;

import com.trabean.travel.callApi.client.AccountClient;
import com.trabean.travel.callApi.client.DemandDepositClient;
import com.trabean.travel.callApi.client.ForeignCurrencyClient;
import com.trabean.travel.callApi.dto.request.AccountBalanceApiRequestDto;
import com.trabean.travel.callApi.dto.request.AccountNumberApiRequestDto;
import com.trabean.travel.callApi.dto.request.AdminUserKeyApiRequestDto;
import com.trabean.travel.callApi.dto.response.AccountBalanceApiResponseDto;
import com.trabean.travel.callApi.dto.response.AccountBalanceApiResponseDto.REC;
import com.trabean.travel.callApi.dto.response.AccountNumberApiResponseDto;
import com.trabean.util.RequestHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonAccountService {

    private final AccountClient accountClient;
    private final DemandDepositClient demandDepositClient;
    private final ForeignCurrencyClient foreignCurrencyClient;

    /**
     * 통장 주인의 userKey 조회
     */
    public String getUserKey(Long parentId) {
        return accountClient.getAdminUserKey(new AdminUserKeyApiRequestDto(parentId)).getUserKey();
    }

    /**
     * 계좌번호 조회
     */
    public String getAccountNo(Long accountId) {
        AccountNumberApiResponseDto accountNumberApiResponseDto = accountClient.getAccount(
                new AccountNumberApiRequestDto(accountId));
        return accountNumberApiResponseDto.getAccountNo();
    }

    /**
     * 원화 계좌 잔액 조회
     */
    public REC getKrwAccountBalance(Long accountId, String accountNo) {
        String adminUserKey = getUserKey(accountId);

        AccountBalanceApiRequestDto getAccountBalanceRequestDto
                = new AccountBalanceApiRequestDto(
                RequestHeader.builder()
                        .apiName("inquireDemandDepositAccountBalance")
                        .userKey(adminUserKey)
                        .build(),
                accountNo);

        AccountBalanceApiResponseDto accountBalanceApiResponseDto = demandDepositClient.getKrwAccountBalance(
                getAccountBalanceRequestDto);

        return accountBalanceApiResponseDto.getRec();
    }

    /**
     * 외화 계좌 잔액 조회
     */
    public Double getForeignAccountBalance(Long accountId, String accountNo) {
        String adminUserKey = getUserKey(accountId);

        AccountBalanceApiRequestDto accountBalanceApiRequestDto = new AccountBalanceApiRequestDto(
                RequestHeader.builder()
                        .apiName("inquireForeignCurrencyDemandDepositAccountBalance")
                        .userKey(adminUserKey)
                        .build(),
                accountNo);

        AccountBalanceApiResponseDto accountBalanceApiResponseDto
                = foreignCurrencyClient.getForeignAccountBalance(accountBalanceApiRequestDto);

        return (double) accountBalanceApiResponseDto.getRec().getAccountBalance();
    }

}
