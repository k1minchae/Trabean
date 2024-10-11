package com.trabean.account.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CreateForeignTravelAccountRequestDTO {

    @JsonProperty("domesticAccountId")
    private Long domesticAccountId;

    @JsonProperty("currency")
    private String currency;
}
