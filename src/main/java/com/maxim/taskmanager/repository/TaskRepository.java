package com.maxim.taskmanager.repository;

import com.maxim.taskmanager.model.entity.Task;
import com.maxim.taskmanager.model.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByProjectId(Integer projectId);
    List<Task> findByAssigneeId(Integer assigneeId);
    List<Task> findByStatus(TaskStatus status);
}