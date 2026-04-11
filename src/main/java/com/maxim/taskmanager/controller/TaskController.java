package com.maxim.taskmanager.controller;

import com.maxim.taskmanager.model.dto.TaskDto.TaskCreateDto;
import com.maxim.taskmanager.model.dto.TaskDto.TaskResponseDto;
import com.maxim.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskCreateDto dto) {
        log.info("POST /api/tasks - создание задачи с названием: {}", dto.getTitle());

        TaskResponseDto task = taskService.createTask(dto);

        log.info("POST /api/tasks - задача создана с id: {}", task.getId());
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Integer id) {
        log.info("GET /api/tasks/{} - поиск задачи", id);
        TaskResponseDto task = taskService.getTaskById(id);
        log.info("GET /api/tasks/{} - задача найдена", id);
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        log.info("GET /api/tasks - получение списка всех задач");
        List<TaskResponseDto> tasks = taskService.getAllTasks();
        log.info("GET /api/tasks - найдено {} задач", tasks.size());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByProjectId(@PathVariable Integer projectId) {
        log.info("GET /api/tasks/project/{} - получение задач проекта", projectId);
        List<TaskResponseDto> tasks = taskService.getTasksByProjectId(projectId);
        log.info("GET /api/tasks/project/{} - найдено {} задач", projectId, tasks.size());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByAssigneeId(@PathVariable Integer assigneeId) {
        log.info("GET /api/tasks/assignee/{} - получение задач пользователя", assigneeId);
        List<TaskResponseDto> tasks = taskService.getTasksByAssigneeId(assigneeId);
        log.info("GET /api/tasks/assignee/{} - найдено {} задач", assigneeId, tasks.size());
        return ResponseEntity.ok(tasks);
    }
}