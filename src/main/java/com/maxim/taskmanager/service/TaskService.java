package com.maxim.taskmanager.service;

import com.maxim.taskmanager.model.dto.TaskDto.TaskCreateDto;
import com.maxim.taskmanager.model.dto.TaskDto.TaskResponseDto;
import com.maxim.taskmanager.model.dto.TaskDto.TaskUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    TaskResponseDto createTask(TaskCreateDto dto);
    TaskResponseDto getTaskById(Integer id);
    Page<TaskResponseDto> getAllTasks(Pageable pageable);
    List<TaskResponseDto> getTasksByProjectId(Integer projectId);
    List<TaskResponseDto> getTasksByAssigneeId(Integer assigneeId);
    TaskResponseDto updateTask(Integer id, TaskUpdateDto dto);
    void deleteTask(Integer id);
}
