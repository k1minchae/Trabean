package com.trabean.travel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberRoleChangeRequestDto {

    private Long userId;
    private Long accountId;
    private String role;

}
