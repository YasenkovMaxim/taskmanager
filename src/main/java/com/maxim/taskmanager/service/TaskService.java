package com.maxim.taskmanager.service;

import com.maxim.taskmanager.model.dto.TaskDto.TaskCreateDto;
import com.maxim.taskmanager.model.dto.TaskDto.TaskResponseDto;

public interface TaskService {
    TaskResponseDto createTask(TaskCreateDto dto);
}
