package com.trabean.verification.dto.request;

import lombok.Getter;

@Getter
public class OneWonVerificationRequestDTO {
    private String userKey;
    private String accountNo;
    private String otp;
}
