package com.maxim.taskmanager.controller;


import com.maxim.taskmanager.model.dto.projectDto.ProjectCreateDto;
import com.maxim.taskmanager.model.dto.projectDto.ProjectResponseDto;
import com.maxim.taskmanager.model.dto.projectDto.ProjectUpdateDto;
import com.maxim.taskmanager.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable Integer id) {
        log.info("GET /projects/{} - поиск проекта", id);
        ProjectResponseDto project = projectService.getProjectById(id);
        log.info("GET projects/{} - проект найден", id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<ProjectResponseDto>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("GET /projects - страница: {}, размер: {}", page, size);
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProjectResponseDto> projectsPage = projectService.getAllProjects(pageable);
        log.info("GET /projects - найдено {} проектов, всего страниц: {}",
                projectsPage.getNumberOfElements(), projectsPage.getTotalPages());
        return ResponseEntity.ok(projectsPage);
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(@Valid @RequestBody ProjectCreateDto dto) {
        log.info("POST projects - создание проекта с названием: {}", dto.getName());
        ProjectResponseDto project = projectService.createProject(dto);
        log.info("POST /projects - проект создан с id: {}", project.getId());
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> updateProject(
            @PathVariable Integer id,
            @Valid @RequestBody ProjectUpdateDto dto) {
        log.info("PUT /projects/{} - обновление проекта", id);
        ProjectResponseDto updatedProject = projectService.updateProject(id, dto);
        log.info("PUT /projects/{} - проект обновлён", id);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer id) {
        log.info("DELETE /projects/{} - удаление проекта", id);
        projectService.deleteProject(id);
        log.info("DELETE /projects/{} - проект удалён", id);
        return ResponseEntity.noContent().build();
    }
}
