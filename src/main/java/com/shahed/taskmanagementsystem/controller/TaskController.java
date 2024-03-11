package com.shahed.taskmanagementsystem.controller;

import com.shahed.taskmanagementsystem.dto.payload.SearchTaskRequest;
import com.shahed.taskmanagementsystem.dto.payload.TaskRequest;
import com.shahed.taskmanagementsystem.jpa.entity.ETaskPriority;
import com.shahed.taskmanagementsystem.jpa.entity.ETaskStatus;
import com.shahed.taskmanagementsystem.jpa.entity.Task;
import com.shahed.taskmanagementsystem.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @Operation(
        security = {
            @SecurityRequirement(name = "Bearer <Access Token>")
        },
        summary = "Get all tasks",
        description = "This endpoint allows a user to retrieve a list of tasks.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "The response content will show a list of tasks"
            )
        }
    )
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<Task> getAllTasks(
        @RequestParam(value = "title", required = false) String title,
        @RequestParam(value = "description", required = false) String description,
        @RequestParam(value = "status", required = false) ETaskStatus status,
        @RequestParam(value = "priority", required = false) ETaskPriority priority,
        @RequestParam(value = "assigneeId", required = false) Long assigneeId,
        @RequestParam(value = "dueDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dueDate,
        @ParameterObject @PageableDefault(size = 10, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        log.trace("Getting all tasks");

        return taskService.findAll(
            SearchTaskRequest.builder()
                .title(title)
                .description(description)
                .status(status)
                .priority(priority)
                .assigneeId(assigneeId)
                .dueDate(dueDate)
                .pageable(pageable)
                .build()
        );
    }

    @Operation(
        security = {
            @SecurityRequirement(name = "Bearer <Access Token>")
        },
        summary = "Create task",
        description = "This endpoint allows a user to create a new task.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "The response content will show the newly created task"
            )
        }
    )
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(
        @RequestBody @Valid TaskRequest task
    ) {
        log.trace("Creating task: {}", task);
        return taskService.saveTask(task);
    }

    @Operation(
        security = {
            @SecurityRequirement(name = "Bearer <Access Token>")
        },
        summary = "Get task",
        description = "This endpoint allows a user to retrieve a task by its id.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "The response content will show the task"
            )
        }
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Task getTask(
        @PathVariable Long id
    ) {
        log.trace("Getting task with id: {}", id);
        return taskService.getTask(id);
    }

    @Operation(
        security = {
            @SecurityRequirement(name = "Bearer <Access Token>")
        },
        summary = "Update task",
        description = "This endpoint allows a user to update a task by its id.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "The response content will show the updated task"
            )
        }
    )
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Task updateTask(
        @PathVariable Long id,
        @RequestBody @Valid TaskRequest task
    ) {
        log.trace("Updating task with id: {}", id);
        return taskService.updateTask(id, task);
    }

    @Operation(
        security = {
            @SecurityRequirement(name = "Bearer <Access Token>")
        },
        summary = "Delete task",
        description = "This endpoint allows a user to delete a task by its id.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "The response content will show the deleted task"
            )
        }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTask(
        @PathVariable Long id
    ) {
        log.trace("Deleting task with id: {}", id);
        taskService.delete(id);
    }
}
