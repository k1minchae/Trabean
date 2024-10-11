package com.trabean.external.msa.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserKeyRequestDTO {

    @JsonProperty("userId")
    private Long userId;
}
