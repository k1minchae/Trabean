package com.trabean.travel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountChangeNameRequestDto {

    private Long accountId;
    private String accountName;

}
