package com.maxim.taskmanager.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
public class ProjectResponseDto {
    private Integer id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private Integer ownerId;  // ID владельца
}