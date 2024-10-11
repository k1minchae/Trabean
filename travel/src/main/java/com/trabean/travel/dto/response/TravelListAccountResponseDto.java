package com.trabean.travel.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelListAccountResponseDto {

    private Long accountId;
    private String accountNo;
    private String accountName;
    private String bankName;
    private List<TravelAccountResponseDto> account;

    @Builder
    public TravelListAccountResponseDto(Long accountId, String accountNo, String accountName, String bankName, List<TravelAccountResponseDto> account) {
        this.accountId = accountId;
        this.accountNo = accountNo;
        this.accountName = accountName;
        this.bankName = bankName;
        this.account = account;
    }

}
