package com.trabean.external.msa.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MainAccountIdResponseDTO {

    @JsonProperty("mainAccountId")
    private Long mainAccountId;
}
