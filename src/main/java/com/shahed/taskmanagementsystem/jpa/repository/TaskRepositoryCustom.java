package com.shahed.taskmanagementsystem.jpa.repository;

import com.shahed.taskmanagementsystem.dto.payload.SearchTaskRequest;
import com.shahed.taskmanagementsystem.jpa.entity.Task;
import org.springframework.data.domain.Page;

public interface TaskRepositoryCustom {
    Page<Task> findByCriteria(SearchTaskRequest searchTaskRequest);
}
