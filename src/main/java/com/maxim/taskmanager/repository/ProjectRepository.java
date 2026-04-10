package com.maxim.taskmanager.repository;

import com.maxim.taskmanager.model.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByOwnerId(Integer ownerId);
}
