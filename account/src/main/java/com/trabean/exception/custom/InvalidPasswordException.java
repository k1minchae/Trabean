package com.trabean.exception.custom;

/**
 * 매번 같은 메시지를 내보내서 싱글턴 패턴 적용
 */
public class InvalidPasswordException extends RuntimeException {

    private static final InvalidPasswordException INSTANCE = new InvalidPasswordException("비밀번호가 틀렸습니다.");

    private InvalidPasswordException(String message) {
        super(message);
    }

    public static InvalidPasswordException getInstance() {
        return INSTANCE;
    }
}
