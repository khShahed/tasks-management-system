package com.shahed.taskmanagementsystem.job;

import com.shahed.taskmanagementsystem.service.EmailSenderService;
import com.shahed.taskmanagementsystem.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSenderScheduler implements Job {
    private final EmailSenderService emailSenderService;
    private final NotificationService notificationService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("EmailSenderScheduler started");

        var notifications = notificationService.getNotificationsToSend();

        log.debug("Found {} notifications", notifications.size());

        for (var notification : notifications) {
            try {
                emailSenderService.sendEmail(notification);
                notificationService.markAsSent(notification);
            } catch (Exception e) {
                log.error("Error sending email for notification: {}", notification.getId(), e);
            }
        }
    }
}
