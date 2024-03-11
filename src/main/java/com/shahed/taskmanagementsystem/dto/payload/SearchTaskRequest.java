package com.shahed.taskmanagementsystem.dto.payload;

import com.shahed.taskmanagementsystem.jpa.entity.ETaskPriority;
import com.shahed.taskmanagementsystem.jpa.entity.ETaskStatus;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchTaskRequest {
    private String title;
    private String description;
    private Long assigneeId;
    private ETaskStatus status;
    private ETaskPriority priority;
    private OffsetDateTime dueDate;
    Pageable pageable;
}
