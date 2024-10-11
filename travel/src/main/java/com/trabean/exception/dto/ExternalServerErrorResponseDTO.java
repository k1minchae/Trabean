package com.trabean.exception.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExternalServerErrorResponseDTO {
    private String message;
}
