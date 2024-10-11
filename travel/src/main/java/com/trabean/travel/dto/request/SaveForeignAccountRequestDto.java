package com.trabean.travel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveForeignAccountRequestDto {

    private Long accountId;
    private String exchangeCurrency;
    private Long parentAccountId;

    public static SaveForeignAccountRequestDto from(Long accountId, String exchangeCurrency, Long parentAccountId) {
        return new SaveForeignAccountRequestDto(accountId, exchangeCurrency, parentAccountId);
    }

}
