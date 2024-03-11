package com.shahed.taskmanagementsystem.controller;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shahed.taskmanagementsystem.dto.payload.TaskRequest;
import com.shahed.taskmanagementsystem.jpa.entity.ETaskPriority;
import com.shahed.taskmanagementsystem.jpa.entity.ETaskStatus;
import com.shahed.taskmanagementsystem.jpa.entity.Task;
import com.shahed.taskmanagementsystem.jpa.repository.HistoryRepository;
import com.shahed.taskmanagementsystem.jpa.repository.TaskRepository;
import com.shahed.taskmanagementsystem.jpa.repository.UserRepository;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TaskControllerIntegrationTest {
    private static final String BASE_URL = "/api/tasks";
    private static final String DEFAULT_USER_NAME = "admin@abc.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
        .withReuse(true);

    @BeforeEach
    void setup() {
        // Add user that can be used in PrePersist
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(DEFAULT_USER_NAME, "pass123");
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // start clean
        taskRepository.deleteAll();
        historyRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        taskRepository.deleteAll();
        historyRepository.deleteAll();
    }

    @Test
    void givenNoParam_whenGetAllTasks_returnExpectedResult() throws Exception {
        create15Tasks();

        // Perform request
        mockMvc.perform(get("/api/tasks")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(10)))
            .andExpect(jsonPath("$.totalElements").value(15))
            .andExpect(jsonPath("$.size").value(10))
            .andExpect(jsonPath("$.totalPages").value(2));
    }

    @Test
    void givenStatusFilter_whenGetAllTasks_returnExpectedResult() throws Exception {
        create15Tasks();

        // Perform request
        mockMvc.perform(get("/api/tasks")
                .param("status", "IN_PROGRESS")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(5)))
            .andExpect(jsonPath("$.totalElements").value(5))
            .andExpect(jsonPath("$.size").value(10))
            .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void givenPriorityFilter_whenGetAllTasks_returnExpectedResult() throws Exception {
        create15Tasks();

        // Perform request
        mockMvc.perform(get("/api/tasks")
                .param("priority", "HIGH")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(3)))
            .andExpect(jsonPath("$.totalElements").value(3))
            .andExpect(jsonPath("$.size").value(10))
            .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void givenValidData_whenCreateTask_returnExpectedResult() throws Exception {
        TaskRequest taskRequest = TaskRequest.builder()
            .title("Test Task")
            .description("This is a test task")
            .build();

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.title").value(taskRequest.getTitle()))
            .andExpect(jsonPath("$.description").value(taskRequest.getDescription()));

        // Verify data in the database
        List<Task> tasks = taskRepository.findAll();
        assertEquals(1, tasks.size());

        Task savedTask = tasks.getFirst();
        assertNotNull(savedTask.getId());
        assertEquals(taskRequest.getTitle(), savedTask.getTitle());
        assertEquals(taskRequest.getDescription(), savedTask.getDescription());
    }

    @Test
    void givenInvalidData_whenCreateTask_returnError() throws Exception {
        TaskRequest taskRequest = TaskRequest.builder()
            .build();

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequest)))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.message").value("Title is required"));
    }

    @Test
    void givenValidData_whenGetTask_returnExpectedResult() throws Exception {
        var existingTask = createTask();
        existingTask = taskRepository.save(existingTask);

        mockMvc.perform(get("%s/{id}".formatted(BASE_URL), existingTask.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(existingTask.getId()))
            .andExpect(jsonPath("$.title").value(existingTask.getTitle()))
            .andExpect(jsonPath("$.description").value(existingTask.getDescription()));
    }

    @Test
    void givenInvalidData_whenGetTask_returnExpectedResult() throws Exception {
        mockMvc.perform(get("%s/{id}".formatted(BASE_URL), 1234L)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void givenInvalidData_whenUpdateTask_returnExpectedResult() throws Exception {
        var existingTask = createTask();
        existingTask = taskRepository.save(existingTask);

        // Prepare update data
        TaskRequest updateRequest = TaskRequest.builder()
            .title("Updated Task")
            .description("This is an updated task")
            .status(ETaskStatus.IN_PROGRESS)
            .priority(ETaskPriority.MEDIUM)
            .dueDate(OffsetDateTime.now().plusDays(7))
            .build();

        // Perform request
        mockMvc.perform(put("/api/tasks/{id}", existingTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(existingTask.getId()))
            .andExpect(jsonPath("$.title").value(updateRequest.getTitle()))
            .andExpect(jsonPath("$.description").value(updateRequest.getDescription()))
            .andExpect(jsonPath("$.status").value(updateRequest.getStatus().name()))
            .andExpect(jsonPath("$.priority").value(updateRequest.getPriority().name()));

        // Verify data in the database
        var history = historyRepository.findAll();
        assertEquals(5, history.size());
    }

    @Test
    void givenInvalidData_whenDeleteTask_returnExpectedResult() throws Exception {
        var savedTask = createTask();
        savedTask = taskRepository.save(savedTask);

        var tasksBeforeDelete = taskRepository.findAll();
        assertEquals(1, tasksBeforeDelete.size());

        // Perform request
        mockMvc.perform(delete("%s/{id}".formatted(BASE_URL), savedTask.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // Verify data in the database
        var tasks = taskRepository.findAll();
        assertEquals(0, tasks.size());
    }

    private Task createTask() {
        var user = userRepository.findByUsername(DEFAULT_USER_NAME)
            .orElse(null);

        return Task.builder()
            .title("Test Task")
            .description("This is a test task")
            .assignee(user)
            .status(ETaskStatus.TO_DO)
            .priority(ETaskPriority.HIGH)
            .build();
    }

    private void create15Tasks() {
        for (int i = 0; i < 15; i++) {
            var user = userRepository.findByUsername(DEFAULT_USER_NAME)
                .orElse(null);

            var task = Task.builder()
                .title("Task %d".formatted(i))
                .description("This is a test task")
                .assignee(user)
                .status(ETaskStatus.TO_DO)
                .priority(ETaskPriority.LOW)
                .build();

            if (i % 3 == 0) {
                task.setStatus(ETaskStatus.IN_PROGRESS);
            }

            if (i % 5 == 0) {
                task.setPriority(ETaskPriority.HIGH);
            }

            taskRepository.save(task);
        }
    }
}