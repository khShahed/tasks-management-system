package com.shahed.taskmanagementsystem.jpa.repository;

import com.shahed.taskmanagementsystem.jpa.entity.Notification;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findTop100BySentAtIsNull();
}
