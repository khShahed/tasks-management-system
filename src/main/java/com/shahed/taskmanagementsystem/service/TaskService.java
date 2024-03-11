package com.shahed.taskmanagementsystem.service;

import com.shahed.taskmanagementsystem.dto.payload.SearchTaskRequest;
import com.shahed.taskmanagementsystem.dto.payload.TaskRequest;
import com.shahed.taskmanagementsystem.jpa.entity.ETaskChange;
import com.shahed.taskmanagementsystem.jpa.entity.Task;
import com.shahed.taskmanagementsystem.jpa.repository.TaskRepository;
import com.shahed.taskmanagementsystem.jpa.repository.UserRepository;
import com.shahed.taskmanagementsystem.mapper.TaskMapper;
import com.shahed.taskmanagementsystem.utils.DateUtils;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final HistoryService historyService;

    public Page<Task> findAll(SearchTaskRequest searchTaskRequest) {
        return taskRepository.findByCriteria(searchTaskRequest);
    }

    public Task saveTask(TaskRequest task) {
        var taskEntity = taskMapper.toTask(task);
        if (task.getAssigneeId() != null) {
            taskEntity.setAssignee(
                userRepository.findById(task.getAssigneeId())
                    .orElse(null)
            );
        }

        return taskRepository.save(taskEntity);
    }

    public Task getTask(Long taskId) {
        return taskRepository.findById(taskId)
            .orElse(null);
    }

    public Task updateTask(Long taskId, TaskRequest task) {
        var existingTask = taskRepository.findById(taskId)
            .orElse(null);

        if (existingTask == null) {
            return null;
        }

        if (!StringUtils.equalsIgnoreCase(existingTask.getTitle(), task.getTitle())) {
            historyService.createHistory(existingTask, task, ETaskChange.TITLE_CHANGE);
            existingTask.setTitle(task.getTitle());
        }

        if (!StringUtils.equalsIgnoreCase(existingTask.getDescription(), task.getDescription())) {
            historyService.createHistory(existingTask, task, ETaskChange.DESCRIPTION_CHANGE);
            existingTask.setDescription(task.getDescription());
        }

        if ((existingTask.getAssignee() == null && task.getAssigneeId() != null)
            || (task.getAssigneeId() != null && !existingTask.getAssignee().getId().equals(task.getAssigneeId()))
        ) {
            historyService.createHistory(existingTask, task, ETaskChange.ASSIGNEE_CHANGE);
            existingTask.setAssignee(
                userRepository.findById(task.getAssigneeId())
                    .orElse(null)
            );
        }

        if (existingTask.getPriority() != task.getPriority()) {
            historyService.createHistory(existingTask, task, ETaskChange.PRIORITY_CHANGE);
            existingTask.setPriority(task.getPriority());
        }

        if (existingTask.getStatus() != task.getStatus()) {
            historyService.createHistory(existingTask, task, ETaskChange.STATUS_CHANGE);
            existingTask.setStatus(task.getStatus());
        }

        var newDueDate = DateUtils.convertOffsetDtoToInternalDto(task.getDueDate());
        if (
            (existingTask.getDueDate() == null && task.getDueDate() != null)
                || (newDueDate != null && !newDueDate.equals(existingTask.getDueDate()))
        ) {
            historyService.createHistory(existingTask, task, ETaskChange.DUE_DATE_CHANGE);
            existingTask.setDueDate(newDueDate);
        }

        return taskRepository.save(existingTask);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> findTasksForNotification(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime notificationThreshold) {

        return taskRepository.tasksToNotify(
            startDate, endDate, notificationThreshold
        );
    }
}
