package com.taskManager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taskManager.exception.UnauthorizedAccessException;
import com.taskManager.modal.Task;
import com.taskManager.modal.TaskStatus;
import com.taskManager.modal.UserDTO;
import com.taskManager.service.TaskServices;
import com.taskManager.service.UserService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskServices taskServices;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody Task task, @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO user = userService.getUserProfile(jwt);
        Task createdTask = taskServices.createTask(task, user.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id, @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO user = userService.getUserProfile(jwt);
        Task task = taskServices.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks(@RequestParam(required = false) TaskStatus status, @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO user = userService.getUserProfile(jwt);
        List<Task> tasks = taskServices.getAllTask(status);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @RequestBody Task updatedTask,
            @RequestHeader("Authorization") String jwt) throws Exception {

        // Get the user profile from the JWT
        UserDTO user = userService.getUserProfile(jwt);

        // Check if the user has the "ROLE_ADMIN"
        if (!"ROLE_ADMIN".equals(user.getRole())) {
            throw new UnauthorizedAccessException("You are not authorized to update this task");
        }

        // Proceed with the task update
        Task task = taskServices.updateTask(id, updatedTask, user.getId(), user.getRole());

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) throws Exception {
        taskServices.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/user/{userId}/assign")
    public ResponseEntity<Task> assignTaskToUser(@PathVariable Long id, @PathVariable Long userId, @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO user = userService.getUserProfile(jwt);
        Task task = taskServices.assignToUser(userId, id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUser(@RequestParam(required = false) TaskStatus status, @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO user = userService.getUserProfile(jwt);
        List<Task> tasks = taskServices.getTasksByUser(user.getId(), status);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable Long id) throws Exception {
        Task task = taskServices.completeTask(id);
        return ResponseEntity.ok(task);
    }
}
