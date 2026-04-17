package com.maxim.taskmanager.service;

import com.maxim.taskmanager.model.dto.projectDto.ProjectCreateDto;
import com.maxim.taskmanager.model.dto.projectDto.ProjectResponseDto;
import com.maxim.taskmanager.model.dto.projectDto.ProjectUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {
    ProjectResponseDto createProject(ProjectCreateDto dto);

    ProjectResponseDto getProjectById(Integer id);

    ProjectResponseDto updateProject(Integer id, ProjectUpdateDto dto);

    void deleteProject(Integer id);

    Page<ProjectResponseDto> getAllProjects(Pageable pageable);
}
