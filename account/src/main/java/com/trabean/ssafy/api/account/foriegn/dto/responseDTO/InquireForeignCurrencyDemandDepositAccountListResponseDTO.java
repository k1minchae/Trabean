package com.trabean.ssafy.api.account.foriegn.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.ResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SSAFY 금융 API p.231 - 계좌 목록 조회 responseDTO
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InquireForeignCurrencyDemandDepositAccountListResponseDTO {

    @JsonProperty("Header")
    private ResponseHeader header;

    @JsonProperty("REC")
    private List<REC> rec;

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
        private Double dailyTransferLimit;
        private Double oneTimeTransferLimit;
        private Double accountBalance;
        private String lastTransactionDate;
        private String currency;
    }
}
