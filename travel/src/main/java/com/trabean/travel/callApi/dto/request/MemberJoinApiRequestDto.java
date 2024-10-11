package com.trabean.travel.callApi.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberJoinApiRequestDto {

    private Long userId;
    private Long domesticAccountId;
    private List<Long> foreignAccountIdList;

}
