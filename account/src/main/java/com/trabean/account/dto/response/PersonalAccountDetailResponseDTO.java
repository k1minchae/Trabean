package com.trabean.account.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.trabean.ssafy.api.response.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonalAccountDetailResponseDTO {
    private ResponseCode responseCode;
    private String responseMessage;

    private String bankName;
    private Long accountBalance;

    private List<Transaction> transactionList;

    @Builder
    @Getter
    public static class Transaction {
        private String transactionType;
        private String transactionSummary;
        private String transactionDate;
        private String transactionTime;
        private Long transactionBalance;
        private Long transactionAfterBalance;
    }
}
