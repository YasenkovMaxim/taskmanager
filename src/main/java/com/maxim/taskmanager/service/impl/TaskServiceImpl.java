package com.maxim.taskmanager.service.impl;

import com.maxim.taskmanager.exception.ProjectNotFoundException;
import com.maxim.taskmanager.exception.UserNotFoundException;
import com.maxim.taskmanager.model.dto.TaskDto.TaskCreateDto;
import com.maxim.taskmanager.model.dto.TaskDto.TaskMapper;
import com.maxim.taskmanager.model.dto.TaskDto.TaskResponseDto;
import com.maxim.taskmanager.model.entity.Project;
import com.maxim.taskmanager.model.entity.Task;
import com.maxim.taskmanager.model.entity.User;
import com.maxim.taskmanager.repository.ProjectRepository;
import com.maxim.taskmanager.repository.TaskRepository;
import com.maxim.taskmanager.repository.UserRepository;
import com.maxim.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TaskResponseDto createTask(TaskCreateDto dto) {
        log.info("Создание задачи с названием: {}", dto.getTitle());
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Проект с id " + dto.getProjectId() + " не найден"));
        User assignee = null;
        if (dto.getAssigneeId() != null) {
            assignee = userRepository.findById(dto.getAssigneeId())
                    .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + dto.getAssigneeId() + " не найден"));
        }
        Task task = TaskMapper.toEntity(dto, project, assignee);
        Task savedTask = taskRepository.save(task);
        log.info("Задача создана с id: {}", savedTask.getId());
        return TaskMapper.toResponseDto(savedTask);
    }
}