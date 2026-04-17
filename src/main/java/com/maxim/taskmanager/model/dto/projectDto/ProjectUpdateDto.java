package com.maxim.taskmanager.model.dto.projectDto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectUpdateDto {

    @Size(min = 1, max = 100)
    private String name;

    private String description;
}