package com.trabean.travel.callApi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trabean.util.ResponseHeader;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExchangeRateApiResponseDto {

    @JsonProperty("Header")
    private ResponseHeader header;

    @JsonProperty("REC")
    private List<RecDetail> rec;

    @Getter
    public static class RecDetail {
        private Long id;
        private String currency;
        private String exchangeRate;
        private double exchangeMin;
        private String created;
    }

}
