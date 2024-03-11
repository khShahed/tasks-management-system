package com.shahed.taskmanagementsystem.mapper;

import com.shahed.taskmanagementsystem.dto.payload.TaskRequest;
import com.shahed.taskmanagementsystem.jpa.entity.Task;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    @Mapping(target = "dueDate", source = "dueDate")
    @Mapping(target = "status", source = "status", defaultValue = "TO_DO")
    @Mapping(target = "priority", source = "priority", defaultValue = "LOW")
    Task toTask(TaskRequest taskRequest);

    default LocalDateTime mapOffsetDateTimeToLocalDateTime(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }

        return offsetDateTime.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }
}
