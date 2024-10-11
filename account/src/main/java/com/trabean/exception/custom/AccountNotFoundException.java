package com.trabean.exception.custom;

/**
 * 매번 같은 메시지를 내보내서 싱글턴 패턴 적용
 */
public class AccountNotFoundException extends RuntimeException {

    private static final AccountNotFoundException INSTANCE = new AccountNotFoundException("해당 통장을 찾지 못했습니다.");

    private AccountNotFoundException(String message) {
        super(message);
    }

    public static AccountNotFoundException getInstance() {
        return INSTANCE;
    }
}
