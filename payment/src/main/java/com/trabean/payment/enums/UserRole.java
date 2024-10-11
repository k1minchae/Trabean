package com.trabean.payment.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {
    ADMIN, PAYER, NONE_PAYER;

    @JsonCreator
    public static UserRole from(String value) {
        return UserRole.valueOf(value.trim().toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}