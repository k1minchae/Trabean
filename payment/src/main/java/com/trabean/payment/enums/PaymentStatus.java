package com.trabean.payment.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentStatus {
    SUCCESS, CANCEL, EXPIRED, PASSWORD_ERROR, BALANCE_ERROR, PENDING;

    @JsonCreator
    public static PaymentStatus from(String value) {
        return PaymentStatus.valueOf(value.trim().toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
