package com.trabean.travel.service;

import com.trabean.travel.callApi.client.AccountClient;
import com.trabean.travel.callApi.client.DemandDepositClient;
import com.trabean.travel.callApi.client.ForeignCurrencyClient;
import com.trabean.travel.callApi.dto.request.GetAccountBalanceRequestDto;
import com.trabean.travel.callApi.dto.request.GetAccountNumberRequestDto;
import com.trabean.travel.callApi.dto.response.GetAccountBalanceResponseDto;
import com.trabean.travel.callApi.dto.response.GetAccountNumberResponseDto;
import com.trabean.travel.dto.response.TravelAccountIdResponseDto;
import com.trabean.travel.dto.response.TravelAccountResponseDto;
import com.trabean.travel.dto.response.TravelListAccountResponseDto;
import com.trabean.travel.entity.ForeignTravelAccount;
import com.trabean.travel.entity.KrwTravelAccount;
import com.trabean.travel.repository.KrwTravelAccountRepository;
import com.trabean.util.RequestHeader;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TravelAccountService {

    private final KrwTravelAccountRepository krwTravelAccountRepository;

    private final AccountClient accountClient;
    private final DemandDepositClient demandDepositClient;
    private final ForeignCurrencyClient foreignCurrencyClient;

    @Value("${api.userKey}")
    private String userKey;

    @Transactional
    public Long updateTravelAccountName(Long accountId, String accountName) {
        KrwTravelAccount account = krwTravelAccountRepository.findByAccountId(accountId);
        account.setAccountName(accountName);
        return accountId;
    }

    public TravelListAccountResponseDto findAllTravelAccount(Long parentId) {
        KrwTravelAccount krwTravelAccount = krwTravelAccountRepository.findByAccountId(parentId);

        if (krwTravelAccount == null) {
            throw new RuntimeException("KRW 계좌를 찾을 수 없습니다.");
        }

        List<TravelAccountResponseDto> list = new ArrayList<>();

        // 계좌번호 조회
        String accountKRWNo = getAccountNo(parentId);

        // krwTravelAccount 잔액조회
        Double accountKRWBalance = 0.0;

        GetAccountBalanceRequestDto getAccountBalanceRequestDto
                = new GetAccountBalanceRequestDto(
                RequestHeader.builder()
                        .apiName("inquireDemandDepositAccountBalance")
                        .userKey(userKey)
                        .build(),
                accountKRWNo);

        GetAccountBalanceResponseDto getAccountBalanceResponseDto = demandDepositClient.getKrwAccountBalance(
                getAccountBalanceRequestDto);

        accountKRWBalance = (double) getAccountBalanceResponseDto.getRec().getAccountBalance();

        list.add(new TravelAccountResponseDto(krwTravelAccount.getAccountId(), "한국", "KRW", accountKRWBalance));

        List<ForeignTravelAccount> foreignTravelAccounts = krwTravelAccount.getChildAccounts();

        for (ForeignTravelAccount foreignTravelAccount : foreignTravelAccounts) {
            Long accountId = foreignTravelAccount.getAccountId();
            String exchangeCurrency = foreignTravelAccount.getExchangeCurrency();
            String country = "";

            if (exchangeCurrency.equals("USD")) {
                country = "미국";
            } else if (exchangeCurrency.equals("EUR")) {
                country = "유럽";
            } else if (exchangeCurrency.equals("JPY")) {
                country = "일본";
            } else if (exchangeCurrency.equals("GBP")) {
                country = "영국";
            } else if (exchangeCurrency.equals("CHF")) {
                country = "스위스";
            } else if (exchangeCurrency.equals("CAD")) {
                country = "캐나다";
            }

            // 외화통장 계좌번호 조회
            String foreignAccountNo = getAccountNo(accountId);

            // 외화통장 잔액조회
            Double foreignAccountBalance = 0.0;

            getAccountBalanceRequestDto = new GetAccountBalanceRequestDto(
                    RequestHeader.builder()
                            .apiName("inquireForeignCurrencyDemandDepositAccountBalance")
                            .userKey(userKey)
                            .build(),
                    foreignAccountNo);

            getAccountBalanceResponseDto = foreignCurrencyClient.getForeignAccountBalance(getAccountBalanceRequestDto);
            foreignAccountBalance = (double) getAccountBalanceResponseDto.getRec().getAccountBalance();

            list.add(new TravelAccountResponseDto(accountId, country, exchangeCurrency, foreignAccountBalance));
        }

        return new TravelListAccountResponseDto(krwTravelAccount.getAccountName(), list);
    }

    public String getAccountNo(Long accountId) {
        GetAccountNumberResponseDto getAccountNumberResponseDto = accountClient.getAccount(
                new GetAccountNumberRequestDto(accountId));
        String accountNo = getAccountNumberResponseDto.getAccountNo();

        return accountNo;
    }

    public TravelAccountIdResponseDto findAccountIdByCurrency(Long parentId, String currency) {
        KrwTravelAccount krwTravelAccount = krwTravelAccountRepository.findByAccountId(parentId);

        if (currency.equals("KRW")) {
            return new TravelAccountIdResponseDto(krwTravelAccount.getAccountId());
        } else {
            List<ForeignTravelAccount> foreignTravelAccounts = krwTravelAccount.getChildAccounts();

            for (ForeignTravelAccount foreignTravelAccount : foreignTravelAccounts) {
                String accountCurreny = foreignTravelAccount.getExchangeCurrency();
                if (currency.equals(accountCurreny)) {
                    return new TravelAccountIdResponseDto(foreignTravelAccount.getAccountId());
                }
            }
        }

        return null;
    }

}
