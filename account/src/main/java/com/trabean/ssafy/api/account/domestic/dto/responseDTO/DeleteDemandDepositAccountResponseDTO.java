package com.trabean.ssafy.api.account.domestic.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.ResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * SSAFY 금융 API p.69 - 계좌 해지 responseDTO
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteDemandDepositAccountResponseDTO {

    @JsonProperty("Header")
    private ResponseHeader header;

    @JsonProperty("REC")
    private REC rec;

    @Getter
    public static class REC {
        private String status;
        private String accountNo;
        private String refundAccountNo;
        private Long accountBalance;
    }
}
