package com.maxim.taskmanager.service;

import com.maxim.taskmanager.exception.ProjectNotFoundException;
import com.maxim.taskmanager.exception.UserNotFoundException;
import com.maxim.taskmanager.model.dto.TaskDto.TaskCreateDto;
import com.maxim.taskmanager.model.dto.TaskDto.TaskResponseDto;
import com.maxim.taskmanager.model.entity.*;
import com.maxim.taskmanager.repository.ProjectRepository;
import com.maxim.taskmanager.repository.TaskRepository;
import com.maxim.taskmanager.repository.UserRepository;
import com.maxim.taskmanager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private TaskCreateDto taskCreateDto;
    private Task task;
    private Project project;
    private User assignee;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setId(1);
        project.setName("Тестовый проект");

        assignee = new User();
        assignee.setId(1);
        assignee.setEmail("assignee@mail.com");

        taskCreateDto = new TaskCreateDto();
        taskCreateDto.setTitle("Тестовая задача");
        taskCreateDto.setDescription("Описание задачи");
        taskCreateDto.setStatus(TaskStatus.TODO);
        taskCreateDto.setPriority(TaskPriority.HIGH);
        taskCreateDto.setProjectId(1);
        taskCreateDto.setAssigneeId(1);

        task = new Task();
        task.setId(1);
        task.setTitle("Тестовая задача");
        task.setDescription("Описание задачи");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setProject(project);
        task.setAssignee(assignee);
    }

    @Test
    void createTask_Success() {
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(userRepository.findById(1)).thenReturn(Optional.of(assignee));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDto result = taskService.createTask(taskCreateDto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Тестовая задача", result.getTitle());
        assertEquals(1, result.getProjectId());
        assertEquals(1, result.getAssigneeId());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void createTask_ProjectNotFound_ThrowsException() {
        when(projectRepository.findById(999)).thenReturn(Optional.empty());

        taskCreateDto.setProjectId(999);

        assertThrows(ProjectNotFoundException.class, () -> {
            taskService.createTask(taskCreateDto);
        });
    }

    @Test
    void createTask_AssigneeNotFound_ThrowsException() {
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        taskCreateDto.setAssigneeId(999);

        assertThrows(UserNotFoundException.class, () -> {
            taskService.createTask(taskCreateDto);
        });
    }
}