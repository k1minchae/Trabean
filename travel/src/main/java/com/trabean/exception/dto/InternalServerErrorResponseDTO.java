package com.trabean.exception.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InternalServerErrorResponseDTO {
    private String message;
}
