package com.trabean.ssafy.api.verification.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.ResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * SSAFY 금융 API p.205 - 1원 송금 검증 responseDTO
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckAuthCodeResponseDTO {

    @JsonProperty("Header")
    private ResponseHeader header;

    @JsonProperty("REC")
    private REC rec;

    @Getter
    public static class REC {
        private String status;
        private Long transactionUniqueNo;
        private String accountNo;
    }
}
