package com.trabean.travel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountHistoryDetail {

    private Long transactionUniqueNo;
    private String transactionDate;
    private String transactionTime;
    private String transactionType;
    private String transactionTypeName;
    private String transactionAccountNo;
    private Double transactionBalance;
    private Double transactionAfterBalance;
    private String transactionSummary;
    private String transactionMemo;

}
