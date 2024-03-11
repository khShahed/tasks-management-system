package com.shahed.taskmanagementsystem.jpa.repository.impl;

import com.shahed.taskmanagementsystem.TestTaskManagementSystemApplication;
import com.shahed.taskmanagementsystem.dto.payload.SearchTaskRequest;
import com.shahed.taskmanagementsystem.jpa.entity.ETaskPriority;
import com.shahed.taskmanagementsystem.jpa.entity.ETaskStatus;
import com.shahed.taskmanagementsystem.jpa.entity.Task;
import com.shahed.taskmanagementsystem.jpa.repository.TaskRepository;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = TestTaskManagementSystemApplication.class)
class TaskRepositoryCustomImplTest {

    @Autowired
    private TaskRepository taskRepository;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private AuthenticationManager authenticationManager;

    private void authenticateUser(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @BeforeEach
    void setup() {
        // Add user that can be used in PrePersist
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken("admin@abc.com", "pass123");
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        taskRepository.deleteAll();

        // Add test data
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("This is task 1");
        task1.setStatus(ETaskStatus.TO_DO);
        task1.setPriority(ETaskPriority.HIGH);
        task1.setDueDate(LocalDateTime.now().plusDays(1));
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("This is task 2");
        task2.setStatus(ETaskStatus.IN_PROGRESS);
        task2.setPriority(ETaskPriority.MEDIUM);
        task2.setDueDate(LocalDateTime.now().plusDays(2));
        taskRepository.save(task2);
    }

    @AfterEach
    void tearDown() {
        taskRepository.deleteAll();
    }

    @Test
    void testFindByCriteria_withTitle() {
        Pageable pageable = PageRequest.of(0, 10);
        SearchTaskRequest request = SearchTaskRequest.builder()
            .title("Task 1")
            .pageable(pageable)
            .build();

        Page<Task> tasks = taskRepository.findByCriteria(request);

        assertNotNull(tasks);
        assertEquals(1, tasks.getTotalElements());
        assertEquals("Task 1", tasks.getContent().get(0).getTitle());
    }

    @Test
    void testFindByCriteria_withStatusAndPriority() {
        Pageable pageable = PageRequest.of(0, 10);
        SearchTaskRequest request = SearchTaskRequest.builder()
            .status(ETaskStatus.IN_PROGRESS)
            .priority(ETaskPriority.MEDIUM)
            .pageable(pageable)
            .build();

        Page<Task> tasks = taskRepository.findByCriteria(request);

        assertNotNull(tasks);
        assertEquals(1, tasks.getTotalElements());
        assertEquals("Task 2", tasks.getContent().get(0).getTitle());
    }

    @Test
    void testFindByCriteria_withDueDate() {
        Pageable pageable = PageRequest.of(0, 10);
        OffsetDateTime dueDate = OffsetDateTime.now(ZoneOffset.UTC).plusDays(1);
        SearchTaskRequest request = SearchTaskRequest.builder()
            .dueDate(dueDate)
            .pageable(pageable)
            .build();

        Page<Task> tasks = taskRepository.findByCriteria(request);

        assertNotNull(tasks);
        assertEquals(1, tasks.getTotalElements());
        assertEquals("Task 2", tasks.getContent().get(0).getTitle());
    }
}