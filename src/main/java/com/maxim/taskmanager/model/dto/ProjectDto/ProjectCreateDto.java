package com.maxim.taskmanager.model.dto.ProjectDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectCreateDto {

    @Schema(example = "Мой первый проект", description = "Название проекта")
    @NotBlank(message = "Название проекта не может быть пустым")
    private String name;

    @Schema(example = "Описание проекта", description = "Описание")
    private String description;

    @Schema(example = "1", description = "ID владельца проекта")
    @NotNull
    private Integer ownerId;
}