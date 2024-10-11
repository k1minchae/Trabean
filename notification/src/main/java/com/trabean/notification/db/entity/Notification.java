package com.trabean.notification.db.entity;


import com.trabean.notification.api.dto.response.NotificationReadRes;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "notifications")
@Data
public class Notification {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    private Long senderId;
    private Long receiverId;
    private Long accountId;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    private boolean isRead;
    private Long amount;

    @CreationTimestamp
    private Timestamp createTime;


    public NotificationReadRes toReadDto() {
        NotificationReadRes notificationReadRes = new NotificationReadRes();
        notificationReadRes.setNotificationId(notificationId);
        notificationReadRes.setAccountId(accountId);
        notificationReadRes.setAmount(amount);
        notificationReadRes.setNotificationType(notificationType);
        notificationReadRes.setSenderId(senderId);
        notificationReadRes.setRead(isRead);
        notificationReadRes.setCreateTime(createTime);
        return notificationReadRes;

    }
}
