package com.yohanii.lostandfound.component.notification.repository;

import com.yohanii.lostandfound.component.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceiverId(Long id);
}
