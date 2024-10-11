package com.trabean.travel.repository;

import com.trabean.travel.entity.KrwTravelAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KrwTravelAccountRepository extends JpaRepository<KrwTravelAccount, Long> {

    KrwTravelAccount findByAccountId(Long accountId);

}
