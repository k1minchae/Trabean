package com.trabean.internal.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TravelAccountMembersRequestDTO {

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("accountId")
    private Long accountId;
}
