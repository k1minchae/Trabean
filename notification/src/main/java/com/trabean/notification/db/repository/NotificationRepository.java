package com.trabean.notification.db.repository;

import com.trabean.notification.db.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverIdOrderByCreateTimeDesc(Long userId);

//    boolean existsByIsRead(boolean isRead);


    boolean existsByReceiverIdAndIsRead(Long userId, boolean isRead);
}
