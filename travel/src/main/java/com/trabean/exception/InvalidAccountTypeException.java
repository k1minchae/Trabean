package com.trabean.exception;

public class InvalidAccountTypeException extends RuntimeException {

    private static final InvalidAccountTypeException INSTANCE = new InvalidAccountTypeException("통장의 종류가 다릅니다.");

    private InvalidAccountTypeException(String message) {
        super(message);
    }

    public static InvalidAccountTypeException getInstance() {
        return INSTANCE;
    }
}
