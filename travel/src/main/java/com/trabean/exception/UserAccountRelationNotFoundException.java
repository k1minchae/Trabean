package com.trabean.exception;

public class UserAccountRelationNotFoundException extends RuntimeException {

    private static final UserAccountRelationNotFoundException INSTANCE = new UserAccountRelationNotFoundException(
            "유저와 통장의 관계를 찾을 수 없습니다.");

    private UserAccountRelationNotFoundException(String message) {
        super(message);
    }

    public static UserAccountRelationNotFoundException getInstance() {
        return INSTANCE;
    }
}
