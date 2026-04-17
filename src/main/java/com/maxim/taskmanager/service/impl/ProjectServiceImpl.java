package com.maxim.taskmanager.service.impl;

import com.maxim.taskmanager.exception.ProjectNotFoundException;
import com.maxim.taskmanager.exception.UserNotFoundException;
import com.maxim.taskmanager.model.dto.ProjectDto.ProjectCreateDto;
import com.maxim.taskmanager.model.dto.ProjectDto.ProjectMapper;
import com.maxim.taskmanager.model.dto.ProjectDto.ProjectResponseDto;
import com.maxim.taskmanager.model.dto.ProjectDto.ProjectUpdateDto;
import com.maxim.taskmanager.model.entity.Project;
import com.maxim.taskmanager.model.entity.User;
import com.maxim.taskmanager.repository.ProjectRepository;
import com.maxim.taskmanager.repository.UserRepository;
import com.maxim.taskmanager.security.SecurityUtils;
import com.maxim.taskmanager.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public ProjectResponseDto createProject(ProjectCreateDto dto) {
        log.info("Создание проекта с названием: {}", dto.getName());
        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + dto.getOwnerId() + " не найден"));
        Project project = ProjectMapper.toEntity(dto, owner);
        Project savedProject = projectRepository.save(project);
        log.info("Проект создан с id: {}", savedProject.getId());
        return ProjectMapper.toResponseDto(savedProject);
    }

    @Override
    public ProjectResponseDto getProjectById(Integer id) {
        log.info("Поиск проекта по id: {}", id);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Проект с id " + id + " не найден"));
        User currentUser = securityUtils.getCurrentUser();
        if (!securityUtils.isAdmin() && currentUser.getId() != project.getOwner().getId()) {
            log.warn("Пользователь {} пытается просмотреть проект {}", currentUser.getEmail(), project.getName());
            throw new RuntimeException("У вас нет прав на просмотр этого проекта");
        }
        log.info("Проект найден: {}", project.getName());
        return ProjectMapper.toResponseDto(project);
    }

    @Override
    public Page<ProjectResponseDto> getAllProjects(Pageable pageable) {
        log.info("Получение проектов с пагинацией: страница {}, размер {}",
                pageable.getPageNumber(), pageable.getPageSize());
        if (!securityUtils.isAdmin()) {
            log.warn("Пользователь {} пытается получить список всех проектов", securityUtils.getCurrentUser().getEmail());
            throw new RuntimeException("У вас нет прав на просмотр всех проектов");
        }
        Page<Project> projectsPage = projectRepository.findAll(pageable);
        log.info("Найдено {} проектов, всего страниц: {}, всего элементов: {}",
                projectsPage.getNumberOfElements(),
                projectsPage.getTotalPages(),
                projectsPage.getTotalElements());
        return projectsPage.map(ProjectMapper::toResponseDto);
    }

    @Override
    @Transactional
    public ProjectResponseDto updateProject(Integer id, ProjectUpdateDto dto) {
        log.info("Обновление проекта с id: {}", id);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Проект с id " + id + " не найден"));
        User currentUser = securityUtils.getCurrentUser();
        if (!securityUtils.isAdmin() && currentUser.getId() != project.getOwner().getId()) {
            log.warn("Пользователь {} пытается обновить проект {}", currentUser.getEmail(), project.getName());
            throw new RuntimeException("У вас нет прав на обновление этого проекта");
        }
        ProjectMapper.updateEntity(project, dto);
        Project updatedProject = projectRepository.save(project);
        log.info("Проект с id {} обновлён", id);
        return ProjectMapper.toResponseDto(updatedProject);
    }

    @Override
    @Transactional
    public void deleteProject(Integer id) {
        log.info("Удаление проекта с id: {}", id);
        User currentUser = securityUtils.getCurrentUser();
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Проект с id " + id + " не найден"));
        if (!securityUtils.isAdmin() && currentUser.getId() != project.getOwner().getId()) {
            log.warn("Пользователь {} пытается удалить проект {}", currentUser.getEmail(), project.getName());
            throw new RuntimeException("У вас нет прав на удаление этого проекта");
        }
        projectRepository.deleteById(id);
        log.info("Проект с id {} удалён", id);
    }
}
