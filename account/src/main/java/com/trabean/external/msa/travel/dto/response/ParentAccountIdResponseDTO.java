package com.trabean.external.msa.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParentAccountIdResponseDTO {

    @JsonProperty("parentAccountId")
    private Long parentAccountId;
}
