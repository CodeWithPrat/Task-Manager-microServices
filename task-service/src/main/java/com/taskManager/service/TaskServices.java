package com.taskManager.service;

import java.util.List;

import com.taskManager.exception.TaskNotFoundException;
import com.taskManager.exception.UnauthorizedAccessException;
import com.taskManager.modal.Task;
import com.taskManager.modal.TaskStatus;

public interface TaskServices {

    Task createTask(Task task, String requestedRole) throws UnauthorizedAccessException;

    Task getTaskById(Long id) throws TaskNotFoundException;

    List<Task> getAllTask(TaskStatus status);

    public Task updateTask(Long id, Task updatedTask, Long userId, String userRole) throws TaskNotFoundException, UnauthorizedAccessException;

    void deleteTask(Long id) throws TaskNotFoundException;

    Task assignToUser(Long userId, Long taskId) throws TaskNotFoundException;

    List<Task> getTasksByUser(Long userId, TaskStatus status) throws TaskNotFoundException;

    Task completeTask(Long taskId) throws TaskNotFoundException;
}
