package com.trabean.travel.callApi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class GetAccountNumberResponseDto {

    private String message;
    private String accountNo;

    public static GetAccountNumberResponseDto from(String message, String accountNo) {
        return new GetAccountNumberResponseDto(
                message,
                accountNo
        );
    }

}
