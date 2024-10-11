package com.trabean.verification.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AccountVerificationRequestDTO {

    @JsonProperty("accountNo")
    private String accountNo;
}
