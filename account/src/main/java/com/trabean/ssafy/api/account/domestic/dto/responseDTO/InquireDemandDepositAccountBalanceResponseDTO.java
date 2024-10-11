package com.trabean.ssafy.api.account.domestic.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.ResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * SSAFY 금융 API p.47 - 계좌 잔액 조회 responseDTO
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InquireDemandDepositAccountBalanceResponseDTO {

    @JsonProperty("Header")
    private ResponseHeader header;

    @JsonProperty("REC")
    private REC rec;

    @Getter
    public static class REC {
        private String bankCode;
        private String accountNo;
        private Long accountBalance;
        private String accountCreatedDate;
        private String accountExpireDate;
        private String lastTransactionDate;
        private String currency;
    }
}
