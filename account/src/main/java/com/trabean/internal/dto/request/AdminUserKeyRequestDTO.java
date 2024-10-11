package com.trabean.internal.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AdminUserKeyRequestDTO {

    @JsonProperty("accountId")
    private Long accountId;
}
