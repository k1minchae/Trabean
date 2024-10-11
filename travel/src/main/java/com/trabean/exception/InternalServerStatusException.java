package com.trabean.exception;

public class InternalServerStatusException extends RuntimeException {
    public InternalServerStatusException(String message) {
        super(message);
    }
}
