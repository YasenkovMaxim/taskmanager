package com.maxim.taskmanager.service;

import com.maxim.taskmanager.model.dto.ProjectDto.ProjectCreateDto;
import com.maxim.taskmanager.model.dto.ProjectDto.ProjectResponseDto;

public interface ProjectService {
    ProjectResponseDto createProject(ProjectCreateDto dto);
}
