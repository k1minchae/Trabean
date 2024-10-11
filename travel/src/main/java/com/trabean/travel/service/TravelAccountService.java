package com.trabean.travel.service;

import static com.trabean.util.CurrencyUtils.changeCurrency;

import com.trabean.travel.callApi.client.BankClient;
import com.trabean.travel.callApi.dto.request.BankCodeApiRequestDto;
import com.trabean.travel.callApi.dto.response.AccountBalanceApiResponseDto.REC;
import com.trabean.travel.callApi.dto.response.BankCodeApiResponseDto.BankInfo;
import com.trabean.travel.dto.response.AccountInfoResponseDto;
import com.trabean.travel.dto.response.ParentsAccountIdResponseDto;
import com.trabean.travel.dto.response.TravelAccountIdResponseDto;
import com.trabean.travel.dto.response.TravelAccountResponseDto;
import com.trabean.travel.dto.response.TravelListAccountResponseDto;
import com.trabean.travel.entity.ForeignTravelAccount;
import com.trabean.travel.entity.KrwTravelAccount;
import com.trabean.travel.repository.ForeignTravelAccountRepository;
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

    private final BankClient bankClient;

    private final CommonAccountService commonAccountService;
    private final ForeignTravelAccountRepository foreignTravelAccountRepository;

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

        String accountKRWNo = commonAccountService.getAccountNo(parentId);

        REC getKrwAccountApi= commonAccountService.getKrwAccountBalance(parentId, accountKRWNo);
        Double accountKRWBalance = (double) getKrwAccountApi.getAccountBalance();

        BankCodeApiRequestDto bankCodeApiRequestDto = new BankCodeApiRequestDto(
                RequestHeader.builder()
                        .apiName("inquireBankCodes")
                        .userKey("")
                        .build()
        );

        String bankCode = getKrwAccountApi.getBankCode();
        String bankName = "트래빈뱅크";
        List<BankInfo> bankInfos = bankClient.getBankCode(bankCodeApiRequestDto).getRec();

        for(BankInfo bankInfo : bankInfos) {
            if(bankCode.equals(bankInfo.getBankCode())) {
                if(bankCode.equals("999")) {
                    bankName = "트래빈뱅크";
                } else {
                    bankName = bankInfo.getBankName();
                }
                break;
            }
        }

        list.add(new TravelAccountResponseDto(krwTravelAccount.getAccountId(), "한국", "KRW", accountKRWBalance));

        List<ForeignTravelAccount> foreignTravelAccounts = krwTravelAccount.getChildAccounts();

        if (!foreignTravelAccounts.isEmpty()) {
            for (ForeignTravelAccount foreignTravelAccount : foreignTravelAccounts) {
                Long accountId = foreignTravelAccount.getAccountId();
                String exchangeCurrency = foreignTravelAccount.getExchangeCurrency();
                String country = changeCurrency(exchangeCurrency);

                // 외화통장 계좌번호 조회
                String foreignAccountNo = commonAccountService.getAccountNo(accountId);

                // 외화통장 잔액조회
                Double foreignAccountBalance = commonAccountService.getForeignAccountBalance(accountId,
                        foreignAccountNo);

                list.add(new TravelAccountResponseDto(accountId, country, exchangeCurrency, foreignAccountBalance));
            }
        }

        return TravelListAccountResponseDto.builder()
                .accountId(parentId)
                .accountNo(accountKRWNo)
                .accountName(krwTravelAccount.getAccountName())
                .bankName(bankName)
                .account(list)
                .build();
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

    public ParentsAccountIdResponseDto getParentsAccountId(Long accountId) {
        KrwTravelAccount parentsAccount = foreignTravelAccountRepository.findByAccountId(accountId).getParentAccount();
        return new ParentsAccountIdResponseDto(parentsAccount.getAccountId());
    }

    @Transactional
    public String getAccountName(Long accountId) {
        KrwTravelAccount krwTravelAccount = krwTravelAccountRepository.findByAccountId(accountId);

        if(krwTravelAccount != null) {
            return krwTravelAccount.getAccountName();
        }

        ForeignTravelAccount foreignTravelAccount = foreignTravelAccountRepository.findByAccountId(accountId);

        if(foreignTravelAccount != null) {
            return foreignTravelAccount.getParentAccount().getAccountName();
        }

        return "개인";
    }
}
