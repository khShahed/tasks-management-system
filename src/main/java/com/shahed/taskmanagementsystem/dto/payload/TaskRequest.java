package com.shahed.taskmanagementsystem.dto.payload;

import com.shahed.taskmanagementsystem.jpa.entity.ETaskPriority;
import com.shahed.taskmanagementsystem.jpa.entity.ETaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 255, message = "Title must be less than 255 characters and greater than 5 characters")
    private String title;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    private Long assigneeId;
    private ETaskStatus status;
    private ETaskPriority priority;
    private OffsetDateTime dueDate;
}
