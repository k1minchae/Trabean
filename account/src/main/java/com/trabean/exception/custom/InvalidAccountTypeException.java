package com.trabean.exception.custom;

/**
 * 매번 같은 메시지를 내보내서 싱글턴 패턴 적용
 */
public class InvalidAccountTypeException extends RuntimeException {

    private static final InvalidAccountTypeException INSTANCE = new InvalidAccountTypeException("통장의 종류가 다릅니다.");

    private InvalidAccountTypeException(String message) {
        super(message);
    }

    public static InvalidAccountTypeException getInstance() {
        return INSTANCE;
    }
}
