package com.shahed.taskmanagementsystem.jpa.repository;

import com.shahed.taskmanagementsystem.jpa.entity.Task;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {
    // Find top 100 tasks with dueDate within the next 24 hours
    // and no entry in the notification table for the task in last 36 hours
    // and the task is not completed
    @Query("SELECT t FROM Task t " +
        "WHERE t.dueDate BETWEEN :start AND :end " +
        "AND t.status != 'DONE' " +
        "AND t.assignee is not null " +
        "AND t.id NOT IN (SELECT n.task.id FROM Notification n WHERE n.createdAt > :notificationThreshold)" +
        "ORDER BY t.dueDate ASC " +
        "LIMIT 100")
    List<Task> tasksToNotify(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        @Param("notificationThreshold") LocalDateTime notificationThreshold
    );
}
