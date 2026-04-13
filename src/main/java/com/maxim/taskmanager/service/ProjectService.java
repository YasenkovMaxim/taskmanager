package com.maxim.taskmanager.service;

import com.maxim.taskmanager.model.dto.ProjectDto.ProjectCreateDto;
import com.maxim.taskmanager.model.dto.ProjectDto.ProjectResponseDto;
import com.maxim.taskmanager.model.dto.ProjectDto.ProjectUpdateDto;

public interface ProjectService {
    ProjectResponseDto createProject(ProjectCreateDto dto);

    ProjectResponseDto getProjectById(Integer id);

    ProjectResponseDto updateProject(Integer id, ProjectUpdateDto dto);

    void deleteProject(Integer id);
}
