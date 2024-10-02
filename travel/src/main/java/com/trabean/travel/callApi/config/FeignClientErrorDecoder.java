package com.trabean.travel.callApi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trabean.exception.InternalServerStatusException;
import com.trabean.travel.callApi.dto.SsafyErrorResponseDTO;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;

public class FeignClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            if (methodKey.startsWith("DemandDepositClient") ||
                    methodKey.startsWith("ExchangeClient") ||
                    methodKey.startsWith("ExchangeRateClient") ||
                    methodKey.startsWith("ForeignCurrencyClient")) {
                SsafyErrorResponseDTO errorResponse = objectMapper.readValue(response.body().asInputStream(),
                        SsafyErrorResponseDTO.class);
                return new CustomFeignClientException(errorResponse);
            }
            return new InternalServerStatusException(methodKey);
        } catch (IOException e) {
            return new RuntimeException(e.getMessage());
        }
    }

}
