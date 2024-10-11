package com.trabean.payment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceRequest {

    @JsonProperty("Header")
    private Header header;  // 필드명은 소문자로, 직렬화 시에는 대문자로 변환

    @JsonProperty("accountNo")
    private String accountNo;


}
