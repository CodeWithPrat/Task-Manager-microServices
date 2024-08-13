package com.taskManager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskManager.modal.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    public List<Task> findByAssignedUserId(Long assignedUserId);
}
