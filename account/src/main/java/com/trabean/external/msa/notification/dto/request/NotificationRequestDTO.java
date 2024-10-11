package com.trabean.external.msa.notification.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class NotificationRequestDTO {

    @JsonProperty("senderId")
    private Long senderId;

    @JsonProperty("receiverId")
    private List<Long> receiverIdList;

    @JsonProperty("accountId")
    private Long accountId;

    @JsonProperty("notificationType")
    private Type notificationType;

    @JsonProperty("amount")
    private Long amount;

    @Getter
    public static enum Type {
        DEPOSIT,
        WITHDRAW,
        AUTH
    }
}
