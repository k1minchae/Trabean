package com.trabean.ssafy.api.account.domestic.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.ResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * SSAFY 금융 API p.53 - 계좌 입금 responseDTO
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDemandDepositAccountDepositResponseDTO {

    @JsonProperty("Header")
    private ResponseHeader header;

    @JsonProperty("REC")
    private REC rec;

    @Getter
    public static class REC {
        private Long transactionUniqueNo;
        private String transactionDate;
    }
}
