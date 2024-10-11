package com.trabean.payment.repository;

import com.trabean.payment.entity.Merchants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantsRepository extends JpaRepository<Merchants, Long> {

}
