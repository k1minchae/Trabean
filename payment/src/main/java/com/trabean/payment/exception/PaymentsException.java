package com.trabean.payment.exception;

import org.springframework.http.HttpStatus;

public class PaymentsException extends RuntimeException {

    private final HttpStatus status;  // HTTP 상태 코드

    // 메시지와 상태 코드를 받는 생성자
    public PaymentsException(String message, HttpStatus status) {
        super(message);  // 예외 메시지를 부모 클래스에 전달
        this.status = status;  // 상태 코드 설정
    }

    // 상태 코드만 받는 생성자 (메시지가 필요 없을 때)
    public PaymentsException(HttpStatus status) {
        this.status = status;  // 상태 코드 설정
    }

    // 상태 코드를 반환하는 메서드
    public HttpStatus getStatus() {
        return status;
    }
}
