package com.maxim.taskmanager.service;

import com.maxim.taskmanager.model.dto.TaskDto.TaskCreateDto;
import com.maxim.taskmanager.model.dto.TaskDto.TaskResponseDto;
import java.util.List;

public interface TaskService {
    TaskResponseDto createTask(TaskCreateDto dto);
    TaskResponseDto getTaskById(Integer id);
    List<TaskResponseDto> getAllTasks();
    List<TaskResponseDto> getTasksByProjectId(Integer projectId);
}
