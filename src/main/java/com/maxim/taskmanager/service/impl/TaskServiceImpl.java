package com.maxim.taskmanager.service.impl;

import com.maxim.taskmanager.exception.ProjectNotFoundException;
import com.maxim.taskmanager.exception.TaskNotFoundException;
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

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public TaskResponseDto getTaskById(Integer id) {
        log.info("Поиск задачи по id: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с id " + id + " не найдена"));
        log.info("Задача найдена: {}", task.getTitle());
        return TaskMapper.toResponseDto(task);
    }

    @Override
    public List<TaskResponseDto> getAllTasks() {
        log.info("Получение списка всех задач");
        List<TaskResponseDto> tasks = taskRepository.findAll().stream()
                .map(TaskMapper::toResponseDto)
                .toList();
        log.info("Найдено {} задач", tasks.size());
        return tasks;
    }

    @Override
    public List<TaskResponseDto> getTasksByProjectId(Integer projectId) {
        log.info("Получение задач проекта с id: {}", projectId);
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException("Проект с id " + projectId + " не найден");
        }
        List<TaskResponseDto> tasks = taskRepository.findByProjectId(projectId).stream()
                .map(TaskMapper::toResponseDto)
                .toList();
        log.info("Найдено {} задач для проекта с id: {}", tasks.size(), projectId);
        return tasks;
    }
}