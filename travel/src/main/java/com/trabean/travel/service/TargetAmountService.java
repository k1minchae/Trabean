package com.trabean.travel.service;

import com.trabean.interceptor.UserHeaderInterceptor;
import com.trabean.travel.callApi.client.AccountClient;
import com.trabean.travel.callApi.client.DemandDepositClient;
import com.trabean.travel.callApi.client.UserClient;
import com.trabean.travel.callApi.dto.request.AccountHistoryApiRequestDto;
import com.trabean.travel.callApi.dto.request.MemberInfoApiRequestDto;
import com.trabean.travel.callApi.dto.response.AccountHistoryApiResponseDto;
import com.trabean.travel.callApi.dto.response.MemberInfoApiResponseDto;
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
    private final UserClient userClient;

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
        Double balance = (double) commonAccountService.getKrwAccountBalance(accountId, accountNo).getAccountBalance();

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

        for (AccountHistoryDetail deposit : depositList) {
            String transactionMemo = deposit.getTransactionMemo();

            if (transactionMemo.isEmpty()) {
                continue;
            }

            Long depositorId = Long.parseLong(transactionMemo);
            Double amount = deposit.getTransactionBalance();

            if (depositMap.containsKey(depositorId)) {
                Double prevAmount = depositMap.get(depositorId);
                depositMap.replace(depositorId, prevAmount + amount);
            } else {
                depositMap.put(depositorId, amount);
            }
        }

        // 멤버 조회
        Long userId = UserHeaderInterceptor.userId.get();
        List<MemberInfo> memberList = new ArrayList<>();

        MemberInfoApiRequestDto memberInfoApiRequestDto = new MemberInfoApiRequestDto(userId, accountId);

        List<MemberDetail> memberInfoApiResponseDto = accountClient.getMemberInfo(memberInfoApiRequestDto).getMembers();

        for (MemberDetail member : memberInfoApiResponseDto) {
            Long memberId = member.getUserId();
            Long mainAccountId = userClient.getMainAccountId(memberId).getMainAccountId();
            String mainAccountNo = null;
            Double memberAmount = 0.0;

            if(mainAccountId != null) {
                mainAccountNo = commonAccountService.getAccountNo(mainAccountId);
            }

            if(depositMap.containsKey(member.getUserId())) {
                memberAmount = depositMap.get(member.getUserId());
            }

            memberList.add(MemberInfo.builder()
                            .userId(member.getUserId())
                            .userName(member.getUserName())
                            .role(member.getRole())
                            .amount(memberAmount)
                            .mainAccountId(mainAccountId)
                            .mainAccountNumber(mainAccountNo)
                    .build());
        }

        return new TargetAmountListResponseDto(targetAmount, balance, memberList);
    }
}
