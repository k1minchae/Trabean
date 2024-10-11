package com.trabean.notification.handler;

import com.trabean.notification.api.controller.NotificationController;
import com.trabean.notification.exception.NotificationReadException;
import com.trabean.notification.exception.NotificationSaveException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice(assignableTypes = NotificationController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(NotificationSaveException.class)
    public ResponseEntity<?> handleNotificationSaveException(final NotificationSaveException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(NotificationReadException.class)
    public ResponseEntity<?> handleNotificationReadException(final NotificationReadException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
