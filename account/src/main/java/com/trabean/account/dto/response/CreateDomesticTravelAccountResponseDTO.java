package com.trabean.account.dto.response;

import com.trabean.ssafy.api.response.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDomesticTravelAccountResponseDTO {
    private ResponseCode responseCode;
    private String responseMessage;
}
