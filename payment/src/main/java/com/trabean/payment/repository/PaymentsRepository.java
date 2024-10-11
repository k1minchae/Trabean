package com.trabean.payment.repository;

import com.trabean.payment.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Long> {

    //    @Query("SELECT p FROM Payments p WHERE p.paymentStatus = :paymentStatus")
//    List<Payments> findAllByPaymentStatus(@Param("paymentStatus") PaymentStatus paymentStatus);

}
