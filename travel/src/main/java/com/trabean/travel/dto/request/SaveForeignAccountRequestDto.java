package com.trabean.travel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveForeignAccountRequestDto {

    private Long accountId;
    private String exchangeCurrency;
    private Long parentAccountId;

}
