package com.trabean.notification.db.entity;

import lombok.Getter;

@Getter
public enum NotificationType {
    INVITE("초대"),
    DEPOSIT("입금"),
    WITHDRAW("출금"),
    PAYMENT("지불"),
    AUTH("인증");

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }
}
