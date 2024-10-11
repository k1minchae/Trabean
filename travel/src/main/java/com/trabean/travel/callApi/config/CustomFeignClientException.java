package com.trabean.travel.callApi.config;

import com.trabean.travel.callApi.dto.ErrorResponseDTO;
import lombok.Getter;

@Getter
public class CustomFeignClientException extends RuntimeException {

    private final ErrorResponseDTO errorResponse;

    public CustomFeignClientException(ErrorResponseDTO errorResponse) {
        this.errorResponse = errorResponse;
    }

}
