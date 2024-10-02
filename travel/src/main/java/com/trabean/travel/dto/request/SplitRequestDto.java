package com.trabean.travel.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SplitRequestDto {

    private int totalAmount;
    private int totalNo;
    private Long withdrawalAccountId;
    private String withdrawalAccountNo;
    private List<String> depositAccountList;

}
