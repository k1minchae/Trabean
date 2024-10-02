package com.trabean.travel.service;

import com.trabean.travel.callApi.client.AccountClient;
import com.trabean.travel.callApi.client.DemandDepositClient;
import com.trabean.travel.callApi.dto.request.AccountHistoryApiRequestDto;
import com.trabean.travel.callApi.dto.response.AccountHistoryApiResponseDto;
import com.trabean.travel.callApi.dto.response.MemberInfoApiResponseDto.MemberDetail;
import com.trabean.travel.dto.response.AccountHistoryDetail;
import com.trabean.travel.dto.response.TargetAmountListResponseDto;
import com.trabean.travel.dto.response.TargetAmountListResponseDto.MemberInfo;
import com.trabean.travel.entity.KrwTravelAccount;
import com.trabean.travel.repository.KrwTravelAccountRepository;
import com.trabean.util.RequestHeader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TargetAmountService {

    private final KrwTravelAccountRepository krwTravelAccountRepository;
    private final CommonAccountService commonAccountService;
    private final DemandDepositClient demandDepositClient;
    private final AccountClient accountClient;

    static class Member {
        private Long userId;
        private String userName;
        private String userRole;
        private Double amount;
    }

    @Transactional
    public Long updateTargetAmount(Long accountId, Long targetAmount) {
        KrwTravelAccount account = krwTravelAccountRepository.findByAccountId(accountId);
        account.changeTargetAmount(targetAmount);
        return accountId;
    }

    public TargetAmountListResponseDto getTargetAmountList(Long accountId) {
        KrwTravelAccount account = krwTravelAccountRepository.findByAccountId(accountId);
        String accountNo = commonAccountService.getAccountNo(accountId);

        // 목표금액 조회
        Long targetAmount = account.getTargetAmount();

        // 잔액 조회
        Double balance = commonAccountService.getKrwAccountBalance(accountId, accountNo);

        // 입금 내역 조회
        String adminUserKey = commonAccountService.getUserKey(accountId);

        AccountHistoryApiRequestDto accountHistoryApiRequestDto = new AccountHistoryApiRequestDto(
                RequestHeader.builder()
                        .apiName("inquireTransactionHistoryList")
                        .userKey(adminUserKey)
                        .build(),
                accountNo,
                "20240101",
                "20241231",
                "M",
                "DESC"
        );

        AccountHistoryApiResponseDto accountHistory
                = demandDepositClient.getKrwAccountHistoryList(accountHistoryApiRequestDto);

        List<AccountHistoryDetail> depositList = accountHistory.getRec().getList();

        HashMap<Long, Double> depositMap = new HashMap<>();
        Long userId = 0L;

        for (AccountHistoryDetail deposit : depositList) {
            if (deposit.getTransactionMemo() == null) {
                continue;
            }

            userId = Long.parseLong(deposit.getTransactionMemo());
            Double amount = deposit.getTransactionBalance();

            if (depositMap.containsKey(userId)) {
                Double prevAmount = depositMap.get(userId);
                depositMap.replace(userId, prevAmount + amount);
            } else {
                depositMap.put(userId, amount);
            }
        }

        // 멤버 조회
        List<MemberInfo> memberList = new ArrayList<>();

        List<MemberDetail> memberInfoApiResponseDto = accountClient.getMemberInfo(accountId, userId).getMembers();
        for (MemberDetail member : memberInfoApiResponseDto) {
            memberList.add(MemberInfo.builder()
                    .userId(member.getUserId())
                    .userName(member.getUserName())
                    .role(member.getRole())
                    .amount(depositMap.get(member.getUserId()))
                    .build());
        }

        return new TargetAmountListResponseDto(targetAmount, balance, memberList);
    }
}
