package com.trabean.travel.callApi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoApiRequestDto {

    private Long userId;
    private Long accountId;

}
