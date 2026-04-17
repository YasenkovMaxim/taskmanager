package com.maxim.taskmanager.model.dto.TaskDto;

import com.maxim.taskmanager.model.entity.TaskPriority;
import com.maxim.taskmanager.model.entity.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class TaskCreateDto {

    @Schema(example = "Сделать домашнее задание", description = "Название задачи")
    @NotBlank
    private String title;

    @Schema(example = "Подробное описание задачи", description = "Описание")
    private String description;

    @Schema(example = "TODO", description = "Статус: TODO, IN_PROGRESS, DONE")
    private TaskStatus status;

    @Schema(example = "HIGH", description = "Приоритет: LOW, MEDIUM, HIGH")
    private TaskPriority priority;

    @Schema(example = "2025-12-31T23:59:59Z", description = "Дедлайн")
    private Instant dueDate;

    @Schema(example = "1", description = "ID проекта")
    @NotNull
    private Integer projectId;

    @Schema(example = "1", description = "ID исполнителя")
    private Integer assigneeId;
}