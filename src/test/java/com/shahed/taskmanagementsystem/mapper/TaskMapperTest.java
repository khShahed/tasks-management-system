package com.shahed.taskmanagementsystem.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.shahed.taskmanagementsystem.dto.payload.TaskRequest;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class TaskMapperTest {
    private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);
    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    void givenValidTaskRequest_whenToTask_returnExpectedResult() {
        TaskRequest taskRequest = easyRandom.nextObject(TaskRequest.class);
        taskRequest.setDueDate(OffsetDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneOffset.ofHours(6)));

        var result = taskMapper.toTask(taskRequest);

        assertEquals(taskRequest.getTitle(), result.getTitle());
        assertEquals(taskRequest.getDescription(), result.getDescription());
        assertEquals(taskRequest.getStatus(), result.getStatus());
        assertEquals(taskRequest.getPriority(), result.getPriority());
    }
}