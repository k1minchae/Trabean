package com.trabean.payment.dto.request;

import com.trabean.payment.util.DateTimeUtil;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Header {
    String apiName;
    String transmissionDate;
    String transmissionTime;
    final String institutionCode = "00100";
    final String fintechAppNo = "001";
    String apiServiceCode;
    String institutionTransactionUniqueNo;
    String apiKey;
    String userKey;


    public static class HeaderBuilder {
        public HeaderBuilder apiName(String apiName) {
            this.apiName = apiName;
            this.transmissionDate = DateTimeUtil.getTransmissionDate();
            this.transmissionTime = DateTimeUtil.getTransmissionTime();
            this.apiServiceCode = apiName;
            this.institutionTransactionUniqueNo = DateTimeUtil.generateUniqueNumber();
            this.apiKey = System.getenv("API_KEY");
            return this;
        }
    }
}
