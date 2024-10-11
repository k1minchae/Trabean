package com.trabean.travel.service;

import com.trabean.travel.entity.KrwTravelAccount;
import com.trabean.travel.repository.KrwTravelAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class KrwTravelAccountService {

    private final KrwTravelAccountRepository krwTravelAccountRepository;

    @Transactional
    public void save(KrwTravelAccount krwTravelAccount) {
        krwTravelAccountRepository.save(krwTravelAccount);
    }

}
