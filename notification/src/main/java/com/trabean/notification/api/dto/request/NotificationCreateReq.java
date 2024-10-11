package com.trabean.notification.api.dto.request;


import com.trabean.notification.db.entity.Notification;
import com.trabean.notification.db.entity.NotificationType;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationCreateReq {

    private Long senderId;
    private List<Long> receiverId;
    private Long accountId;
    private NotificationType notificationType;
    private Long amount;


    public List<Notification> toEntityList() {
        List<Notification> notifications = new ArrayList<>();
        for (Long id : receiverId) {
            Notification notification = new Notification();
            notification.setAccountId(accountId);
            notification.setAmount(amount);
            notification.setNotificationType(notificationType);
            notification.setReceiverId(id);  // 각 수신자에 대해 알림 생성
            notification.setSenderId(senderId);
            notifications.add(notification);
        }
        return notifications;
    }
}
