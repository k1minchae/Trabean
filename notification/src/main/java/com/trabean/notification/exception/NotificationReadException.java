package com.trabean.notification.exception;


public class NotificationReadException extends RuntimeException {

    public static final NotificationReadException instance = new NotificationReadException("올바른 알림 ID값이 존재하지 않습니다.");

    private NotificationReadException(String message) {
        super(message);
    }


}
