package com.trabean.exception;

import com.trabean.exception.dto.SsafyServerErrorResponseDTO;
import lombok.Getter;

@Getter
public class CustomFeignClientException extends RuntimeException {

    private final SsafyServerErrorResponseDTO errorResponse;

    public CustomFeignClientException(SsafyServerErrorResponseDTO errorResponse) {
        this.errorResponse = errorResponse;
    }

}
