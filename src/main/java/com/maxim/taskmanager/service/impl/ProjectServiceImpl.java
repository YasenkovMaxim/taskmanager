package com.maxim.taskmanager.service.impl;

import com.maxim.taskmanager.exception.UserNotFoundException;
import com.maxim.taskmanager.model.dto.ProjectDto.ProjectCreateDto;
import com.maxim.taskmanager.model.dto.ProjectDto.ProjectMapper;
import com.maxim.taskmanager.model.dto.ProjectDto.ProjectResponseDto;
import com.maxim.taskmanager.model.entity.Project;
import com.maxim.taskmanager.model.entity.User;
import com.maxim.taskmanager.repository.ProjectRepository;
import com.maxim.taskmanager.repository.UserRepository;
import com.maxim.taskmanager.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

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
}
