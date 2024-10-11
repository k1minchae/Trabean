package com.trabean.external.msa.travel.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SaveDomesticTravelAccountRequestDTO {

    @JsonProperty("accountId")
    private Long accountId;

    @JsonProperty("accountName")
    private String accountName;

    @JsonProperty("targetAmount")
    private Long targetAmount;
}
