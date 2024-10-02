package com.trabean.exception;

public class InvalidPasswordException extends RuntimeException {

    private static final InvalidPasswordException INSTANCE = new InvalidPasswordException("비밀번호가 틀렸습니다.");

    private InvalidPasswordException(String message) {
        super(message);
    }

    public static InvalidPasswordException getInstance() {
        return INSTANCE;
    }
}
