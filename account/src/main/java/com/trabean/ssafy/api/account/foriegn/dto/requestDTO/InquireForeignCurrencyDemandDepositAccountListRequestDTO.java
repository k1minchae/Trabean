package com.trabean.ssafy.api.account.foriegn.dto.requestDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.RequestHeader;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * SSAFY 금융 API p.231 - 계좌 목록 조회 requestDTO
 */
@Builder
@Getter
public class InquireForeignCurrencyDemandDepositAccountListRequestDTO {

    @JsonProperty("Header")
    private RequestHeader header;

    @JsonProperty("currency")
    private List<String> currency;
}
