package com.maxim.taskmanager.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectCreateDto {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Integer ownerId;
}
