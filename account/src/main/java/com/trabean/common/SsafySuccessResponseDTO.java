package com.trabean.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * SSAFY 금융 API 에서 요청 성공 시 반환하는 responseDTO
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SsafySuccessResponseDTO {

    @JsonProperty("responseCode")
    private ResponseCode responseCode;

    @JsonProperty("responseMessage")
    private String responseMessage;
}
