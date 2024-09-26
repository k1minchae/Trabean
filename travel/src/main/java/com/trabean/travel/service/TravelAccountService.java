package com.trabean.travel.service;

import static com.trabean.util.CurrencyUtils.changeCurrency;

import com.trabean.travel.callApi.client.DemandDepositClient;
import com.trabean.travel.callApi.dto.request.AccountBalanceApiRequestDto;
import com.trabean.travel.callApi.dto.response.AccountBalanceApiResponseDto;
import com.trabean.travel.dto.response.AccountInfoResponseDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TravelAccountService {

    private final KrwTravelAccountRepository krwTravelAccountRepository;

    private final DemandDepositClient demandDepositClient;

    private final CommonAccountService commonAccountService;

    private String userKey = "9e10349e-91e9-474d-afb4-564b24178d9f";

    @Transactional
    public Long updateTravelAccountName(Long accountId, String accountName) {
        KrwTravelAccount account = krwTravelAccountRepository.findByAccountId(accountId);
        account.changeAccountName(accountName);
        return accountId;
    }

    public TravelListAccountResponseDto findAllTravelAccount(Long parentId) {
        KrwTravelAccount krwTravelAccount = krwTravelAccountRepository.findByAccountId(parentId);

        if (krwTravelAccount == null) {
            throw new RuntimeException("KRW 계좌를 찾을 수 없습니다.");
        }

        List<TravelAccountResponseDto> list = new ArrayList<>();

        // 계좌번호 조회
        String accountKRWNo = commonAccountService.getAccountNo(parentId);

        // krwTravelAccount 잔액조회
        Double accountKRWBalance = 0.0;

        AccountBalanceApiRequestDto getAccountBalanceRequestDto
                = new AccountBalanceApiRequestDto(
                RequestHeader.builder()
                        .apiName("inquireDemandDepositAccountBalance")
                        .userKey(userKey)
                        .build(),
                accountKRWNo);

        AccountBalanceApiResponseDto accountBalanceApiResponseDto = demandDepositClient.getKrwAccountBalance(
                getAccountBalanceRequestDto);

        accountKRWBalance = (double) accountBalanceApiResponseDto.getRec().getAccountBalance();

        list.add(new TravelAccountResponseDto(krwTravelAccount.getAccountId(), "한국", "KRW", accountKRWBalance));

        List<ForeignTravelAccount> foreignTravelAccounts = krwTravelAccount.getChildAccounts();

        for (ForeignTravelAccount foreignTravelAccount : foreignTravelAccounts) {
            Long accountId = foreignTravelAccount.getAccountId();
            String exchangeCurrency = foreignTravelAccount.getExchangeCurrency();
            String country = changeCurrency(exchangeCurrency);

            // 외화통장 계좌번호 조회
            String foreignAccountNo = commonAccountService.getAccountNo(accountId);

            // 외화통장 잔액조회
            Double foreignAccountBalance = commonAccountService.getForeignAccountBalance(foreignAccountNo);

            list.add(new TravelAccountResponseDto(accountId, country, exchangeCurrency, foreignAccountBalance));
        }

        return new TravelListAccountResponseDto(krwTravelAccount.getAccountName(), list);
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

    public AccountInfoResponseDto getInfo(Long accountId) {
        KrwTravelAccount account = krwTravelAccountRepository.findByAccountId(accountId);
        return new AccountInfoResponseDto(account.getAccountName(), account.getTargetAmount());
    }

    public List<Long> getChildList(Long accountId) {
        List<ForeignTravelAccount> childAccounts = krwTravelAccountRepository.findByAccountId(accountId)
                .getChildAccounts();
        List<Long> list = new ArrayList<>();

        for (ForeignTravelAccount account : childAccounts) {
            list.add(account.getAccountId());
        }

        return list;
    }
}
