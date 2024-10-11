package com.trabean.payment.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentsException.class)
    public ResponseEntity<ErrorResponse> handlePaymentsException(PaymentsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }
}
