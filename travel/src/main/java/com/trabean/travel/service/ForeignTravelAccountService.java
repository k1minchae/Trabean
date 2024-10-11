package com.trabean.travel.service;

import static com.trabean.util.CurrencyUtils.changeCurrency;

import com.trabean.travel.callApi.client.AccountClient;
import com.trabean.travel.callApi.client.ForeignCurrencyClient;
import com.trabean.travel.callApi.dto.request.AccountHistoryApiRequestDto;
import com.trabean.travel.callApi.dto.response.AccountHistoryApiResponseDto;
import com.trabean.travel.dto.request.ForeignAccountHistoryRequestDto;
import com.trabean.travel.dto.request.SaveForeignAccountRequestDto;
import com.trabean.travel.dto.response.AccountHistoryDetail;
import com.trabean.travel.dto.response.ForeignAccountHistoryResponseDto;
import com.trabean.travel.entity.ForeignTravelAccount;
import com.trabean.travel.entity.KrwTravelAccount;
import com.trabean.travel.repository.ForeignTravelAccountRepository;
import com.trabean.travel.repository.KrwTravelAccountRepository;
import com.trabean.util.RequestHeader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ForeignTravelAccountService {

    private final ForeignTravelAccountRepository foreignTravelAccountRepository;
    private final KrwTravelAccountRepository krwTravelAccountRepository;

    private final AccountClient accountClient;
    private final ForeignCurrencyClient foreignCurrencyClient;

    private final CommonAccountService commonAccountService;

    @Transactional
    public void save(SaveForeignAccountRequestDto saveForeignAccountRequestDto) {
        Long parentAccountId = saveForeignAccountRequestDto.getParentAccountId();
        KrwTravelAccount parentAccount = krwTravelAccountRepository.findByAccountId(parentAccountId);

        ForeignTravelAccount foreignTravelAccount = ForeignTravelAccount.builder()
                .accountId(saveForeignAccountRequestDto.getAccountId())
                .exchangeCurrency(saveForeignAccountRequestDto.getExchangeCurrency())
                .parentAccount(parentAccount)
                .build();

        foreignTravelAccountRepository.save(foreignTravelAccount);
    }

    public ForeignAccountHistoryResponseDto findForeignAccountHistory(
            ForeignAccountHistoryRequestDto foreignAccountHistoryRequestDto) {
        Long accountId = foreignAccountHistoryRequestDto.getAccountId();

        String exchangeCurrency = foreignTravelAccountRepository.findByAccountId(accountId).getExchangeCurrency();
        String country = changeCurrency(exchangeCurrency);
        String accountNo = commonAccountService.getAccountNo(accountId);
        Double accountBalance = commonAccountService.getForeignAccountBalance(accountId, accountNo);

        // 외화 계좌 거래 내역 조회
        String adminUserKey = commonAccountService.getUserKey(accountId);

        AccountHistoryApiRequestDto accountHistoryApiRequestDto =
                new AccountHistoryApiRequestDto(
                        RequestHeader.builder()
                                .apiName("inquireForeignCurrencyTransactionHistoryList")
                                .userKey(adminUserKey)
                                .build(),
                        accountNo,
                        foreignAccountHistoryRequestDto.getStartDate(),
                        foreignAccountHistoryRequestDto.getEndDate(),
                        foreignAccountHistoryRequestDto.getTransactionType(),
                        "DESC");

        AccountHistoryApiResponseDto accountHistoryApiResponseDto = foreignCurrencyClient.getForeignAccountHistoryList(
                accountHistoryApiRequestDto);

        String totalCount = accountHistoryApiResponseDto.getRec().getTotalCount();
        List<AccountHistoryDetail> list = accountHistoryApiResponseDto.getRec().getList();

        return new ForeignAccountHistoryResponseDto(
                country, exchangeCurrency, accountBalance, totalCount, list);
    }


}
