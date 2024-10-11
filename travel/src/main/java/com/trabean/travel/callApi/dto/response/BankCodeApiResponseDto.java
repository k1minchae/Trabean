package com.trabean.travel.callApi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.ResponseHeader;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BankCodeApiResponseDto {

    @JsonProperty("Header")
    private ResponseHeader header;

    @JsonProperty("REC")
    private List<BankInfo> rec;

    @Getter
    public static class BankInfo {
        private String bankCode;
        private String bankName;
    }

}
