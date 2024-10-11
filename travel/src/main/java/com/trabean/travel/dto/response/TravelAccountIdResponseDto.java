package com.trabean.travel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TravelAccountIdResponseDto {

    private Long foreignTravelAccountId;

    public static TravelAccountIdResponseDto from(Long foreignTravelAccountId) {
        return new TravelAccountIdResponseDto(foreignTravelAccountId);
    }

}
