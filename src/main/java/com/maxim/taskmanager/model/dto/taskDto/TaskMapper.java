package com.maxim.taskmanager.model.dto.taskDto;

import com.maxim.taskmanager.model.entity.Project;
import com.maxim.taskmanager.model.entity.Task;
import com.maxim.taskmanager.model.entity.User;

public class TaskMapper {

    public static TaskResponseDto toResponseDto(Task task) {
        if (task == null) {
            return null;
        }
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setDueDate(task.getDueDate());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        if (task.getProject() != null) {
            dto.setProjectId(task.getProject().getId());
        }
        if (task.getAssignee() != null) {
            dto.setAssigneeId(task.getAssignee().getId());
        }
        return dto;
    }

    public static Task toEntity(TaskCreateDto dto, Project project, User assignee) {
        if (dto == null) {
            return null;
        }
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());
        task.setProject(project);
        task.setAssignee(assignee);
        return task;
    }

    public static void updateEntity(Task task, TaskUpdateDto dto) {
        if (dto.getTitle() != null) {
            task.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            task.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            task.setStatus(dto.getStatus());
        }
        if (dto.getPriority() != null) {
            task.setPriority(dto.getPriority());
        }
        if (dto.getDueDate() != null) {
            task.setDueDate(dto.getDueDate());
        }
    }
}