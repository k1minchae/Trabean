package com.trabean.exception.dto;

import com.trabean.common.ResponseCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SsafyServerErrorResponseDTO {
    private ResponseCode responseCode;
    private String responseMessage;
}
