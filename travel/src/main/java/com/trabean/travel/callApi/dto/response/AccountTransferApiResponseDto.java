package com.trabean.travel.callApi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.ResponseHeader;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountTransferApiResponseDto {

    @JsonProperty("Header")
    private ResponseHeader header;

    @JsonProperty("REC")
    private List<TransactionInfo> rec;

    @Getter
    public static class TransactionInfo {
        private Long transactionUniqueNo;
        private String accountNo;
        private String transactionDate;
        private String transactionType;
        private String transactionTypeName;
        private String transactionAccountNo;
    }

}
