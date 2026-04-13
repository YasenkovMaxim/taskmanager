package com.maxim.taskmanager.repository;

import com.maxim.taskmanager.model.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

}
