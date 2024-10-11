package com.trabean.travel.callApi.config;

import com.trabean.travel.callApi.dto.SsafyErrorResponseDTO;
import lombok.Getter;

@Getter
public class CustomFeignClientException extends RuntimeException {

    private final SsafyErrorResponseDTO errorResponse;

    public CustomFeignClientException(SsafyErrorResponseDTO errorResponse) {
        this.errorResponse = errorResponse;
    }

}
