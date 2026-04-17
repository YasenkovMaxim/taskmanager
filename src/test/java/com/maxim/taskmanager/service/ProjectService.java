package com.maxim.taskmanager.service;

import com.maxim.taskmanager.exception.UserNotFoundException;
import com.maxim.taskmanager.model.dto.ProjectDto.ProjectCreateDto;
import com.maxim.taskmanager.model.dto.ProjectDto.ProjectResponseDto;
import com.maxim.taskmanager.model.entity.Project;
import com.maxim.taskmanager.model.entity.Role;
import com.maxim.taskmanager.model.entity.User;
import com.maxim.taskmanager.repository.ProjectRepository;
import com.maxim.taskmanager.repository.UserRepository;
import com.maxim.taskmanager.security.SecurityUtils;
import com.maxim.taskmanager.service.impl.ProjectServiceImpl;
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
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private ProjectCreateDto projectCreateDto;
    private Project project;
    private User owner;
    private User currentUser;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1);
        owner.setEmail("owner@mail.com");
        owner.setRole(Role.USER);

        currentUser = new User();
        currentUser.setId(1);
        currentUser.setEmail("owner@mail.com");
        currentUser.setRole(Role.USER);

        projectCreateDto = new ProjectCreateDto();
        projectCreateDto.setName("Тестовый проект");
        projectCreateDto.setDescription("Описание проекта");
        projectCreateDto.setOwnerId(1);

        project = new Project();
        project.setId(1);
        project.setName("Тестовый проект");
        project.setDescription("Описание проекта");
        project.setOwner(owner);
    }

    @Test
    void createProject_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(owner));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponseDto result = projectService.createProject(projectCreateDto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Тестовый проект", result.getName());
        assertEquals(1, result.getOwnerId());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void createProject_OwnerNotFound_ThrowsException() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        projectCreateDto.setOwnerId(999);

        assertThrows(UserNotFoundException.class, () -> {
            projectService.createProject(projectCreateDto);
        });
    }
}
