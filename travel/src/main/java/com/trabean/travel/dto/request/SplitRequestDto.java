package com.trabean.travel.dto.request;

import com.trabean.travel.dto.response.ExchangeResponseDto.AccountInfo;
import com.trabean.travel.dto.response.ExchangeResponseDto.ExchangeCurrency;
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
    private List<Account> depositAccountList;

    @Getter
    public static class Account {
        private Long userId;
        private String accountNumber;
    }

}
