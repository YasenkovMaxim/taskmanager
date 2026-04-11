package com.maxim.taskmanager.service.impl;

import com.maxim.taskmanager.exception.ProjectNotFoundException;
import com.maxim.taskmanager.exception.TaskNotFoundException;
import com.maxim.taskmanager.exception.UserNotFoundException;
import com.maxim.taskmanager.model.dto.TaskDto.TaskCreateDto;
import com.maxim.taskmanager.model.dto.TaskDto.TaskMapper;
import com.maxim.taskmanager.model.dto.TaskDto.TaskResponseDto;
import com.maxim.taskmanager.model.dto.TaskDto.TaskUpdateDto;
import com.maxim.taskmanager.model.entity.Project;
import com.maxim.taskmanager.model.entity.Task;
import com.maxim.taskmanager.model.entity.User;
import com.maxim.taskmanager.repository.ProjectRepository;
import com.maxim.taskmanager.repository.TaskRepository;
import com.maxim.taskmanager.repository.UserRepository;
import com.maxim.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<TaskResponseDto> getAllTasks(Pageable pageable) {
        log.info("Получение задач с пагинацией: страница {}, размер {}, сортировка {}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort());
        Page<TaskResponseDto> tasksPage = taskRepository.findAll(pageable)
                .map(TaskMapper::toResponseDto);
        log.info("Найдено {} задач, всего страниц: {}, всего элементов: {}",
                tasksPage.getNumberOfElements(),
                tasksPage.getTotalPages(),
                tasksPage.getTotalElements());
        return tasksPage;
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

    @Override
    public List<TaskResponseDto> getTasksByAssigneeId(Integer assigneeId) {
        log.info("Получение задач пользователя с id: {}", assigneeId);
        if (!userRepository.existsById(assigneeId)) {
            throw new UserNotFoundException("Пользователь с id " + assigneeId + " не найден");
        }
        List<TaskResponseDto> tasks = taskRepository.findByAssigneeId(assigneeId).stream()
                .map(TaskMapper::toResponseDto)
                .toList();
        log.info("Найдено {} задач для пользователя с id: {}", tasks.size(), assigneeId);
        return tasks;
    }

    @Override
    @Transactional
    public TaskResponseDto updateTask(Integer id, TaskUpdateDto dto) {
        log.info("Обновление задачи с id: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с id " + id + " не найдена"));
        if (dto.getAssigneeId() != null) {
            User assignee = userRepository.findById(dto.getAssigneeId())
                    .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + dto.getAssigneeId() + " не найден"));
            task.setAssignee(assignee);
        }
        TaskMapper.updateEntity(task, dto);
        Task updatedTask = taskRepository.save(task);
        log.info("Задача с id {} обновлена", id);
        return TaskMapper.toResponseDto(updatedTask);
    }

    @Override
    @Transactional
    public void deleteTask(Integer id) {
        log.info("Удаление задачи с id: {}", id);
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Задача с id " + id + " не найдена");
        }
        taskRepository.deleteById(id);
        log.info("Задача с id {} удалена", id);
    }
}