package com.trabean.notification.api.service;


import com.trabean.notification.api.dto.request.NotificationCreateReq;
import com.trabean.notification.api.dto.response.NotificationReadRes;

import java.util.List;

public interface NotificationService {

    public void saveNotification(NotificationCreateReq notificationCreateReq);

    public List<NotificationReadRes> findByUserId(Long userId);

    void updateIsReadById(Long notificationId);

    boolean getStatus(Long userId);
}
