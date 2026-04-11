package com.maxim.taskmanager.controller;


import com.maxim.taskmanager.model.dto.ProjectDto.ProjectCreateDto;
import com.maxim.taskmanager.model.dto.ProjectDto.ProjectResponseDto;
import com.maxim.taskmanager.model.dto.ProjectDto.ProjectUpdateDto;
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

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable Integer id) {
        log.info("GET /api/projects/{} - поиск проекта", id);
        ProjectResponseDto project = projectService.getProjectById(id);
        log.info("GET /api/projects/{} - проект найден", id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(@Valid @RequestBody ProjectCreateDto dto) {
        log.info("POST /api/projects - создание проекта с названием: {}", dto.getName());
        ProjectResponseDto project = projectService.createProject(dto);
        log.info("POST /api/projects - проект создан с id: {}", project.getId());
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> updateProject(
            @PathVariable Integer id,
            @Valid @RequestBody ProjectUpdateDto dto) {
        log.info("PUT /api/projects/{} - обновление проекта", id);
        ProjectResponseDto updatedProject = projectService.updateProject(id, dto);
        log.info("PUT /api/projects/{} - проект обновлён", id);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer id) {
        log.info("DELETE /api/projects/{} - удаление проекта", id);
        projectService.deleteProject(id);
        log.info("DELETE /api/projects/{} - проект удалён", id);
        return ResponseEntity.noContent().build();
    }
}
