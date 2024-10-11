package com.trabean.account.repository;

import com.trabean.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNo(String accountNo);

    @Modifying
    @Query("UPDATE Account a SET a.password = :password WHERE a.accountId = :accountId")
    void updatePasswordById(Long accountId, String password);
}
