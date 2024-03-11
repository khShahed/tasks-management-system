package com.shahed.taskmanagementsystem.job;

import com.shahed.taskmanagementsystem.jpa.entity.Task;
import com.shahed.taskmanagementsystem.service.NotificationService;
import com.shahed.taskmanagementsystem.service.TaskService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpcomingDueDateNotificationScheduler implements Job {
    private final TaskService taskService;
    private final NotificationService notificationService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("UpcomingDueDateNotificationScheduler started");

        // Find tasks not done and due date within the next 24 hours
        // Also not sent a notification in the last 36 hours
        // This is to avoid sending multiple notifications for the same task
        // And to send notification for task that had it due date changed
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dueDateThreshold = now.plusHours(24);
        LocalDateTime notificationThreshold = now.minusHours(36);
        log.debug("Now: {}, due date Threshold: {}, Notification Threshold: {}", now, dueDateThreshold, notificationThreshold);

        List<Task> upcomingTasks = taskService.findTasksForNotification(now, dueDateThreshold, notificationThreshold);

        log.debug("Found {} tasks with upcoming due date", upcomingTasks.size());

        for (Task task : upcomingTasks) {
            log.debug("Task: {} has upcoming due date", task.getTitle());
            notificationService.createNotification(task);
        }
    }
}
