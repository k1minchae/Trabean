package com.trabean.ssafy.api.account.domestic.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.ResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * SSAFY 금융 API p.42 - 계좌 조회 (단건) responseDTO
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InquireDemandDepositAccountResponseDTO {

    @JsonProperty("Header")
    private ResponseHeader header;

    @JsonProperty("REC")
    private REC rec;

    @Getter
    public static class REC {
        private String bankCode;
        private String bankName;
        private String userName;
        private String accountNo;
        private String accountName;
        private String accountTypeCode;
        private String accountTypeName;
        private String accountCreateDate;
        private String accountExpiryDate;
        private Long dailyTransferLimit;
        private Long oneTimeTransferLimit;
        private Long accountBalance;
        private String lastTransactionDate;
        private String currency;
    }
}
