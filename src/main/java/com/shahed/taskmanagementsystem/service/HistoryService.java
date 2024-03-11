package com.shahed.taskmanagementsystem.service;

import com.shahed.taskmanagementsystem.dto.payload.TaskRequest;
import com.shahed.taskmanagementsystem.jpa.entity.ETaskChange;
import com.shahed.taskmanagementsystem.jpa.entity.History;
import com.shahed.taskmanagementsystem.jpa.entity.Task;
import com.shahed.taskmanagementsystem.jpa.repository.HistoryRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService {

    private final UserService userService;
    private final HistoryRepository historyRepository;

    public void createHistory(Task oldTask, TaskRequest newTask, ETaskChange change) {
        log.debug("Saving history for task id: {}", oldTask.getId());

        var history = History.builder()
            .user(userService.getAuthenticatedUser())
            .task(oldTask)
            .action(getChangeMessage(oldTask, newTask, change))
            .timestamp(LocalDateTime.now())
            .build();

        historyRepository.save(history);
    }

    public String getChangeMessage(Task oldTask, TaskRequest newTask, ETaskChange change) {
        log.debug("Getting change message for task id: {}, change: {}", oldTask.getId(), change);

        return switch (change) {
            case TITLE_CHANGE -> new StringBuilder()
                .append("Title changed from ")
                .append(oldTask.getTitle()).append(" to ")
                .append(newTask.getTitle()).toString();
            case DESCRIPTION_CHANGE -> new StringBuilder()
                .append("Description changed from ")
                .append(oldTask.getDescription()).append(" to ")
                .append(newTask.getDescription()).toString();
            case PRIORITY_CHANGE -> new StringBuilder()
                .append("Priority changed from ")
                .append(oldTask.getPriority()).append(" to ")
                .append(newTask.getPriority()).toString();
            case STATUS_CHANGE -> new StringBuilder()
                .append("Status changed from ")
                .append(oldTask.getStatus()).append(" to ")
                .append(newTask.getStatus()).toString();
            case ASSIGNEE_CHANGE -> new StringBuilder()
                .append("Assignee changed from ")
                .append(oldTask.getAssignee().getId()).append(" to ")
                .append(newTask.getAssigneeId()).toString();
            case DUE_DATE_CHANGE -> new StringBuilder()
                .append("Due Date changed from ")
                .append(oldTask.getDueDate()).append(" to ")
                .append(newTask.getDueDate()).toString();
            default -> "";
        };
    }
}
