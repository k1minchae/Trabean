package com.trabean.travel.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ForeignAccountHistoryResponseDto {

    private String country;
    private String exchangeCurrency;
    private Double accountBalance;
    private String totalCount;
    private List<AccountHistoryDetail> list;

}
