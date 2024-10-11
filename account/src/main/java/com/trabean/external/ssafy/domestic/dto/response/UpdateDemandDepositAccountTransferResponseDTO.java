package com.trabean.external.ssafy.domestic.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.ResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SSAFY 금융 API p.56 - 계좌 이체 responseDTO
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDemandDepositAccountTransferResponseDTO {

    @JsonProperty("Header")
    private ResponseHeader header;

    @JsonProperty("REC")
    private List<REC> rec;

    @Getter
    public static class REC {
        private Long transactionUniqueNo;
        private String accountNo;
        private String transactionDate;
        private String transactionType;
        private String transactionTypeName;
        private String transactionAccountNo;
    }
}
