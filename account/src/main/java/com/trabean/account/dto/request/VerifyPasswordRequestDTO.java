package com.trabean.account.dto.request;

import lombok.Getter;

@Getter
public class VerifyPasswordRequestDTO {
    private Long accountId;
    private String accountNo;
    private String password;
}
