package com.trabean.exception;

public class UnauthorizedUserRoleException extends RuntimeException {

    private static final UnauthorizedUserRoleException INSTANCE = new UnauthorizedUserRoleException("권한이 없는 유저입니다.");

    private UnauthorizedUserRoleException(String message) {
        super(message);
    }

    public static UnauthorizedUserRoleException getInstance() {
        return INSTANCE;
    }
}
