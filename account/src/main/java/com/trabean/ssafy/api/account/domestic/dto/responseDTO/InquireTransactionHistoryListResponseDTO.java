package com.trabean.ssafy.api.account.domestic.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.ResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SSAFY 금융 API p.62 - 계좌 거래 내역 조회 responseDTO
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InquireTransactionHistoryListResponseDTO {

    @JsonProperty("Header")
    private ResponseHeader header;

    @JsonProperty("REC")
    private REC rec;

    @Getter
    public static class REC {
        private String totalCount;
        private List<Item> list;
    }

    @Getter
    public static class Item {
        private Long transactionUniqueNo;
        private String transactionDate;
        private String transactionTime;
        private String transactionType;
        private String transactionTypeName;
        private String transactionAccountNo;
        private Long transactionBalance;
        private Long transactionAfterBalance;
        private String transactionSummary;
        private String transactionMemo;
    }
}
