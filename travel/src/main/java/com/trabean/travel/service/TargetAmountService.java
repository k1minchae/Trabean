package com.trabean.travel.service;

import com.trabean.travel.entity.KrwTravelAccount;
import com.trabean.travel.repository.KrwTravelAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TargetAmountService {

    private final KrwTravelAccountRepository krwTravelAccountRepository;

    @Transactional
    public Long updateTargetAmount(Long accountId, Long targetAmount) {
        KrwTravelAccount account = krwTravelAccountRepository.findByAccountId(accountId);
        account.setTargetAmount(targetAmount);
        return accountId;
    }

}
