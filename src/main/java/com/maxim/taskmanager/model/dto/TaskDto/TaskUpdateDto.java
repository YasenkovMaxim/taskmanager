package com.maxim.taskmanager.model.dto.TaskDto;

import com.maxim.taskmanager.model.entity.TaskPriority;
import com.maxim.taskmanager.model.entity.TaskStatus;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class TaskUpdateDto {

    @Size(min = 1, max = 100)
    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private Instant dueDate;

    private Integer assigneeId;
}