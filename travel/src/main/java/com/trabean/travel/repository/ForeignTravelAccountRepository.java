package com.trabean.travel.repository;

import com.trabean.travel.entity.ForeignTravelAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForeignTravelAccountRepository extends JpaRepository<ForeignTravelAccount, Long> {

    ForeignTravelAccount findByAccountId(Long accountId);

}
