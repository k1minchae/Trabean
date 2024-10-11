package com.trabean.notification.api.dto.response;


import com.trabean.notification.db.entity.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class NotificationReadRes {

    private Long notificationId;
    private Long senderId;
    private Long accountId;
    private boolean isRead;
    private NotificationType notificationType;
    private Long amount;
    private Timestamp createTime;


}
