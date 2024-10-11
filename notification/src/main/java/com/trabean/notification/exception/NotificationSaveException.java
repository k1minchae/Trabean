package com.trabean.notification.exception;


public class NotificationSaveException extends RuntimeException {

    public static final NotificationSaveException instance = new NotificationSaveException("알림 저장에 실패하였습니다.");

    private NotificationSaveException(String message) {
        super(message);
    }
}
