package com.trabean.travel.callApi.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRoleUpdateApiRequestDto {

    private Long userId;
    private Long domesticAccountId;
    private List<Long> foreignAccountIdList;
    private String userRole;

}
