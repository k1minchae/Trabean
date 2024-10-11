package com.trabean.account.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TravelAccountCoupleResponseDTO {

    @JsonProperty("domesticAccountId")
    private Long domesticAccountId;

    @JsonProperty("domesticAccountNo")
    private String domesticAccountNo;

    @JsonProperty("foreignAccountId")
    private Long foreignAccountId;

    @JsonProperty("foreignAccountNo")
    private String foreignAccountNo;
}
