package com.trabean.travel.service;

import com.trabean.travel.dto.request.SaveForeignAccountRequestDto;
import com.trabean.travel.entity.ForeignTravelAccount;
import com.trabean.travel.entity.KrwTravelAccount;
import com.trabean.travel.repository.ForeignTravelAccountRepository;
import com.trabean.travel.repository.KrwTravelAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ForeignTravelAccountService {

    private final ForeignTravelAccountRepository foreignTravelAccountRepository;
    private final KrwTravelAccountRepository krwTravelAccountRepository;

    @Transactional
    public void save(SaveForeignAccountRequestDto saveForeignAccountRequestDto) {
        Long parentAccountId = saveForeignAccountRequestDto.getParentAccountId();
        KrwTravelAccount parentAccount = krwTravelAccountRepository.findByAccountId(parentAccountId);

        ForeignTravelAccount foreignTravelAccount = new ForeignTravelAccount();
        foreignTravelAccount.setAccountId(saveForeignAccountRequestDto.getAccountId());
        foreignTravelAccount.setExchangeCurrency(saveForeignAccountRequestDto.getExchangeCurrency());
        foreignTravelAccount.setParentAccount(parentAccount);
        foreignTravelAccountRepository.save(foreignTravelAccount);
    }

}
