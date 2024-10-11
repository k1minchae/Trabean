package com.trabean.verification.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class OneWonVerificationRequestDTO {

    @JsonProperty("accountNo")
    private String accountNo;

    @JsonProperty("otp")
    private String otp;
}
