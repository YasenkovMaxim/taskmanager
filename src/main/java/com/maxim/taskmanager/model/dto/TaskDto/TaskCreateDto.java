package com.maxim.taskmanager.model.dto.TaskDto;

import com.maxim.taskmanager.model.entity.TaskPriority;
import com.maxim.taskmanager.model.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
public class TaskCreateDto {

    @NotBlank
    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private Instant dueDate;

    @NotNull
    private Integer projectId;

    private Integer assigneeId;
}