package com.trabean.util;

import com.trabean.account.domain.Account;
import com.trabean.account.domain.UserAccountRelation;
import com.trabean.account.domain.UserAccountRelation.UserRole;
import com.trabean.exception.custom.AccountNotFoundException;
import com.trabean.exception.custom.InvalidAccountTypeException;
import com.trabean.exception.custom.UnauthorizedUserRoleException;
import com.trabean.exception.custom.UserAccountRelationNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ValidationUtil {

    /**
     * Account 테이블에서 해당 통장이 존재하는지 체크
     *
     * <li>통장이 존재하면 Account 리턴</li>
     * <li>통장이 존재하지 않으면 AccountNotFoundException(404) 예외 발생</li>
     *
     * @param account
     * @return account
     */
    public static Account validateAccount(Optional<Account> account) {
        return account.orElseThrow(AccountNotFoundException::getInstance);
    }

    /**
     * UserAccountRelation 테이블에서 해당 유저와 통장의 관계가 존재하는지 체크
     *
     * <li>관계가 존재하면 UserAccountRelation 리턴</li>
     * <li>관계가 존재하지 않으면 UserAccountRelationNotFoundException(404) 예외 발생</li>
     *
     * @param userAccountRelation
     */
    public static UserAccountRelation validateUserAccountRelation(Optional<UserAccountRelation> userAccountRelation) {
        return userAccountRelation.orElseThrow(UserAccountRelationNotFoundException::getInstance);
    }

    /**
     * UserAccountRelation 테이블에서 해당 통장과 관계가 있는 튜플들이 존재하는지 체크
     *
     * <li>관계가 존재하면 UserAccountRelation List 리턴</li>
     * <li>관계가 존재하지 않으면 빈 List 리턴</li>
     *
     * @param userAccountRelationList
     * @return List<UserAccountRelation>
     */
    public static List<UserAccountRelation> validateUserAccountRelationList(Optional<List<UserAccountRelation>> userAccountRelationList) {
        return userAccountRelationList.orElse(new ArrayList<>());
    }

    /**
     * Client의 요청이 유효한 요청인지 체크
     *
     * <pre>DTO 필드</pre>
     * <li>(필수O) Optional<Account> account</li>
     * <li>(필수O) Optional<UserAccountRelation> userAccountRelation</li>
     * <li>(필수X) AccountType accountType</li>
     * <li>(필수X) UserRole userRole</li>
     * <li>(필수X) Boolean isPayable</li>
     *
     * <pre>사용 방법</pre>
     * <li>Optional<Account> account 는 해당 통장이 DB에 존재하는지 체크</li>
     * <li>Optional<UserAccountRelation> userAccountRelation 는 해당 관계가 DB에 존재하는지 체크</li>
     * <li>AccountType accountType 은 조회된 통장과 타입이 일치하는지 체크</li>
     * <li>UserRole userRole 은 조회된 관계와 유저 권한이 일치하는지 체크</li>
     * <li>Boolean isPayable 은 결제가 가능한 유저인치 체크</li>
     *
     * <pre>예외</pre>
     * <li>통장이 존재하지 않으면 AccountNotFoundException(404) 예외 발생</li>
     * <li>관계가 존재하지 않으면 UserAccountRelationNotFoundException(404) 예외 발생</li>
     * <li>통장이 존재하는데 타입이 일치하지 않으면 InvalidAccountTypeException(401) 예외 발생</li>
     * <li>관계가 존재하는데 유저 권한이 일치하지 않으면 UnauthorizedUserRoleException(401) 예외 발생</li>
     * <li>결제가 불가능한 유저면 UnauthorizedUserRoleException(401) 예외 발생</li>
     *
     * @param validateInputDTO
     * @return account
     */
    public static Account validateInput(ValidateInputDTO validateInputDTO) {
        Account account = validateInputDTO.getAccount().orElseThrow(AccountNotFoundException::getInstance);
        UserAccountRelation userAccountRelation = validateInputDTO.getUserAccountRelation().orElseThrow(UserAccountRelationNotFoundException::getInstance);

        if (validateInputDTO.getAccountType() != null && validateInputDTO.getAccountType() != account.getAccountType()) {
            throw InvalidAccountTypeException.getInstance();
        }

        if (validateInputDTO.getUserRole() != null && validateInputDTO.getUserRole() != userAccountRelation.getUserRole()) {
            throw UnauthorizedUserRoleException.getInstance();
        }

        if (Boolean.TRUE.equals(validateInputDTO.getIsPayable()) && userAccountRelation.getUserRole() == UserRole.NONE_PAYER) {
            throw UnauthorizedUserRoleException.getInstance();
        }

        return account;
    }

}
