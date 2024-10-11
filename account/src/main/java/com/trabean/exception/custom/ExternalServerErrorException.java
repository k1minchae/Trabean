package com.trabean.exception.custom;

/**
 * 다른 서버 Feign Client 에서 발생하는 예외
 */
public class ExternalServerErrorException extends RuntimeException {
    public ExternalServerErrorException(String message) {
        super(message);
    }
}
