package com.trabean.ssafy.api.account.domestic.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.ResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * SSAFY 금융 API p.35 - 계좌 생성 responseDTO
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDemandDepositAccountResponseDTO {

    @JsonProperty("Header")
    private ResponseHeader header;

    @JsonProperty("REC")
    private REC rec;

    @Getter
    public static class REC {
        private String bankCode;
        private String accountNo;
        private Currency currency;
    }

    @Getter
    public static class Currency {
        private String currency;
        private String currencyName;
    }
}
