package com.maxim.taskmanager.model.dto.taskDto;

import com.maxim.taskmanager.model.entity.TaskPriority;
import com.maxim.taskmanager.model.entity.TaskStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class TaskResponseDto {
    private Integer id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Instant dueDate;
    private Instant createdAt;
    private Instant updatedAt;
    private Integer projectId;
    private Integer assigneeId;
}