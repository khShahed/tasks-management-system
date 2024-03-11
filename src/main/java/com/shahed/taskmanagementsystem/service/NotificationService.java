package com.shahed.taskmanagementsystem.service;

import com.shahed.taskmanagementsystem.jpa.entity.Notification;
import com.shahed.taskmanagementsystem.jpa.entity.Task;
import com.shahed.taskmanagementsystem.jpa.repository.NotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final String DEFAULT_SUBJECT = "You have a new notification";
    private final String DEFAULT_MESSAGE = "You task '%s' is due in 24 hours. Please complete it.";

    public void createNotification(Task task) {
        var notification = Notification.builder()
                .task(task)
                .user(task.getAssignee())
                .subject(DEFAULT_SUBJECT)
                .message(DEFAULT_MESSAGE.formatted(task.getTitle()))
                .build();

        notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsToSend(){
        return notificationRepository.findTop100BySentAtIsNull();
    }

    public void markAsSent(Notification notification){
        notification.setSentAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }
}
