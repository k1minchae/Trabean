package com.trabean.travel.callApi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetAccountNumberRequestDto {

    private Long accountId;

    public GetAccountNumberRequestDto from(Long accountId) {
        return new GetAccountNumberRequestDto(accountId);
    }

}
