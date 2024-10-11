package com.trabean.external.msa.travel.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SaveForeignTravelAccountRequestDTO {

    @JsonProperty("accountId")
    private Long foreignAccountId;

    @JsonProperty("parentAccountId")
    private Long domesticAccountId;

    @JsonProperty("exchangeCurrency")
    private String currency;
}
