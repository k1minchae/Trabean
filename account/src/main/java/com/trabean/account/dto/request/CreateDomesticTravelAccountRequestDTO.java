package com.trabean.account.dto.request;

import lombok.Getter;

@Getter
public class CreateDomesticTravelAccountRequestDTO {
    private String userKey;
    private Long userId;
    private String password;
}
