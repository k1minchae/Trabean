package com.trabean.travel.callApi.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationApiRequestDto {

    private Long senderId;
    private List<Long> receiverId;
    private Long accountId;
    private String notificationType;
    private Long amount;

    @Builder
    public NotificationApiRequestDto(Long senderId, List<Long> receiverId, Long accountId, String notificationType, Long amount) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.accountId = accountId;
        this.notificationType = notificationType;
        this.amount = amount;
    }

}
