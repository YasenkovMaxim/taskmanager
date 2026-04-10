package com.maxim.taskmanager.controller;


import com.maxim.taskmanager.model.dto.ProjectDto.ProjectCreateDto;
import com.maxim.taskmanager.model.dto.ProjectDto.ProjectResponseDto;
import com.maxim.taskmanager.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;



    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(@Valid @RequestBody ProjectCreateDto dto) {
        log.info("POST /api/projects - создание проекта с названием: {}", dto.getName());

        ProjectResponseDto project = projectService.createProject(dto);

        log.info("POST /api/projects - проект создан с id: {}", project.getId());
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }
}
