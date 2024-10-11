package com.trabean.util;

import com.trabean.account.domain.Account;
import com.trabean.account.domain.Account.AccountType;
import com.trabean.account.domain.UserAccountRelation;
import com.trabean.account.domain.UserAccountRelation.UserRole;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Builder
@Getter
public class ValidateInputDTO {
    private Optional<Account> account;
    private Optional<UserAccountRelation> userAccountRelation;
    private AccountType accountType;
    private UserRole userRole;
    private Boolean isPayable;
}
