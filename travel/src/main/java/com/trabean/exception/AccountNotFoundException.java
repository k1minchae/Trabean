package com.trabean.exception;

public class AccountNotFoundException extends RuntimeException {

    private static final AccountNotFoundException INSTANCE = new AccountNotFoundException("해당 통장을 찾지 못했습니다.");

    private AccountNotFoundException(String message) {
        super(message);
    }

    public static AccountNotFoundException getInstance() {
        return INSTANCE;
    }
}
