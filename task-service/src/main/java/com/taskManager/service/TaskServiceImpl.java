package com.taskManager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskManager.exception.TaskNotFoundException;
import com.taskManager.exception.UnauthorizedAccessException;
import com.taskManager.modal.Task;
import com.taskManager.modal.TaskStatus;
import com.taskManager.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskServices {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Task createTask(Task task, String requestedRole) throws UnauthorizedAccessException {
        if (!"ROLE_ADMIN".equals(requestedRole)) {
            throw new UnauthorizedAccessException("Only ADMIN can create tasks");
        }
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    @Override
    public Task getTaskById(Long id) throws TaskNotFoundException {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    @Override
    public List<Task> getAllTask(TaskStatus status) {
        return taskRepository.findAll().stream()
                .filter(task -> status == null || task.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public Task updateTask(Long id, Task updatedTask, Long userId, String userRole) throws TaskNotFoundException, UnauthorizedAccessException {
        Task task = getTaskById(id);

        // Check if the user has the "ROLE_ADMIN" before proceeding with the update
        if (!"ROLE_ADMIN".equals(userRole)) {
            throw new UnauthorizedAccessException("You are not authorized to update this task");
        }

        if (updatedTask.getTitle() != null) {
            task.setTitle(updatedTask.getTitle());
        }

        if (updatedTask.getDescription() != null) {
            task.setDescription(updatedTask.getDescription());
        }

        if (updatedTask.getTags() != null && !updatedTask.getTags().isEmpty()) {
            task.setTags(updatedTask.getTags());
        }

        if (updatedTask.getDeadline() != null) {
            task.setDeadline(updatedTask.getDeadline());
        }

        if (updatedTask.getImage() != null) {
            task.setImage(updatedTask.getImage());
        }

        if (updatedTask.getStatus() != null) {
            task.setStatus(updatedTask.getStatus());
        }

        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) throws TaskNotFoundException {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }

    @Override
    public Task assignToUser(Long userId, Long taskId) throws TaskNotFoundException {
        Task task = getTaskById(taskId);
        task.setAssignedUserId(userId);
        task.setStatus(TaskStatus.ASSIGNED);
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getTasksByUser(Long userId, TaskStatus status) throws TaskNotFoundException {
        return taskRepository.findByAssignedUserId(userId).stream()
                .filter(task -> status == null || task.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public Task completeTask(Long taskId) throws TaskNotFoundException {
        Task task = getTaskById(taskId);
        task.setStatus(TaskStatus.DONE);
        return taskRepository.save(task);
    }
}
