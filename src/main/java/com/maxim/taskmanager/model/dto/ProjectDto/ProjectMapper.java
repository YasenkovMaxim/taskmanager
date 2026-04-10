package com.maxim.taskmanager.model.dto.ProjectDto;

import com.maxim.taskmanager.model.entity.Project;
import com.maxim.taskmanager.model.entity.User;

public class ProjectMapper {

    public static ProjectResponseDto toResponseDto(Project project) {
        if (project == null) {
            return null;
        }
        ProjectResponseDto dto = new ProjectResponseDto();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());

        if (project.getOwner() != null) {
            dto.setOwnerId(project.getOwner().getId());
        }
        return dto;
    }

    public static Project toEntity(ProjectCreateDto dto, User owner) {
        if (dto == null) {
            return null;
        }

        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setOwner(owner);
        return project;
    }


    public static void updateEntity(Project project, ProjectUpdateDto dto) {
        if (dto.getName() != null) {
            project.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            project.setDescription(dto.getDescription());
        }
    }
}
