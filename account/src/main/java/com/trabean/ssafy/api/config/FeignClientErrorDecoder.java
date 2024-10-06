package com.trabean.ssafy.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trabean.ssafy.api.ErrorResponseDTO;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;

public class FeignClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ErrorResponseDTO errorResponse = objectMapper.readValue(response.body().asInputStream(), ErrorResponseDTO.class);
            return new CustomFeignClientException(errorResponse);
        } catch (IOException e) {
            return new RuntimeException(e.getMessage());
        }
    }

}
