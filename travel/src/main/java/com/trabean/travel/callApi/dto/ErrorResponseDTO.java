package com.trabean.travel.callApi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {
    private String responseCode;
    private String responseMessage;
}
