package com.trabean.travel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ForeignAccountHistoryRequestDto {

    private Long accountId;
    private String startDate;
    private String endDate;
    private String transactionType;

}
