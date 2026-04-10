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
}