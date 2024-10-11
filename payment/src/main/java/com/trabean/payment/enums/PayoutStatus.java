package com.trabean.payment.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PayoutStatus {
    SUCCESS, FAILED, PENDING;

    @JsonCreator
    public static PayoutStatus from(String value) {
        return PayoutStatus.valueOf(value.trim().toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}