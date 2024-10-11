package com.trabean.ssafy.api.account.domestic.dto.requestDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.RequestHeader;
import lombok.Builder;
import lombok.Getter;

/**
 * SSAFY 금융 API p.38 - 계좌 목록 조회 requestDTO
 */
@Builder
@Getter
public class InquireDemandDepositAccountListRequestDTO {

    @JsonProperty("Header")
    private RequestHeader header;
}
