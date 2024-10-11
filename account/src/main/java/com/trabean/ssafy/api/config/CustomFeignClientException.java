package com.trabean.ssafy.api.config;

import com.trabean.ssafy.api.ErrorResponseDTO;
import lombok.Getter;

@Getter
public class CustomFeignClientException extends RuntimeException {

    private final ErrorResponseDTO errorResponse;

    public CustomFeignClientException(ErrorResponseDTO errorResponse) {
        this.errorResponse = errorResponse;
    }

}
