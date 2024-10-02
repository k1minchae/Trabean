package com.trabean.travel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountChangeTargetAmountRequestDto {

    private Long accountId;
    private Long targetAmount;

}
