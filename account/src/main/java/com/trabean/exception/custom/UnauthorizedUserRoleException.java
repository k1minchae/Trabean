package com.trabean.exception.custom;

/**
 * 매번 같은 메시지를 내보내서 싱글턴 패턴 적용
 */
public class UnauthorizedUserRoleException extends RuntimeException {

    private static final UnauthorizedUserRoleException INSTANCE = new UnauthorizedUserRoleException("권한이 없는 유저입니다.");

    private UnauthorizedUserRoleException(String message) {
        super(message);
    }

    public static UnauthorizedUserRoleException getInstance() {
        return INSTANCE;
    }
}
