package com.trabean.travel.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TravelListAccountResponseDto {

    private String accountName;
    private List<TravelAccountResponseDto> account;

    public static TravelListAccountResponseDto from(String accountName, List<TravelAccountResponseDto> account) {
        return new TravelListAccountResponseDto(accountName, account);
    }

}
