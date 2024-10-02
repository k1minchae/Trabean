package com.trabean.travel.service;

import com.trabean.travel.callApi.client.DemandDepositClient;
import com.trabean.travel.callApi.dto.request.AccountTransferApiRequestDto;
import com.trabean.travel.callApi.dto.response.AccountTransferApiResponseDto;
import com.trabean.travel.dto.request.SaveKrwAccountRequestDto;
import com.trabean.travel.dto.request.SplitRequestDto;
import com.trabean.travel.entity.KrwTravelAccount;
import com.trabean.travel.repository.KrwTravelAccountRepository;
import com.trabean.util.RequestHeader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KrwTravelAccountService {

    private final KrwTravelAccountRepository krwTravelAccountRepository;

    private final DemandDepositClient demandDepositClient;
    private final CommonAccountService commonAccountService;

    private String userKey = "9e10349e-91e9-474d-afb4-564b24178d9f";

    @Transactional
    public void save(SaveKrwAccountRequestDto saveKrwAccountRequestDto) {
        KrwTravelAccount account = KrwTravelAccount.builder()
                .accountId(saveKrwAccountRequestDto.getAccountId())
                .accountName(saveKrwAccountRequestDto.getAccountName())
                .targetAmount(saveKrwAccountRequestDto.getTargetAmount())
                .build();

        krwTravelAccountRepository.save(account);
    }

    public void splitAmount(SplitRequestDto splitRequestDto) {
        String adminUserKey = commonAccountService.getUserKey(splitRequestDto.getWithdrawalAccountId());

        int totalAmount = splitRequestDto.getTotalAmount();
        int totalNo = splitRequestDto.getTotalNo();
        Long amount = Long.valueOf(totalAmount / totalNo);

        List<String> depositAccountList = splitRequestDto.getDepositAccountList();

        for (String depositAccount : depositAccountList) {
            AccountTransferApiRequestDto accountTransferApiRequestDto = new AccountTransferApiRequestDto(
                    RequestHeader.builder()
                            .apiName("updateDemandDepositAccountTransfer")
                            .userKey(adminUserKey)
                            .build(),
                    depositAccount,
                    "여행통장 N빵",
                    amount,
                    splitRequestDto.getWithdrawalAccountNo(),
                    "N빵 나누기"
            );

            AccountTransferApiResponseDto accountTransferApiResponseDto
                    = demandDepositClient.transferAccount(accountTransferApiRequestDto);
        }
    }
}
