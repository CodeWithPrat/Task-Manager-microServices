package com.taskManager.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController {

     // Get tasks by user ID, optionally filtering by status
    @GetMapping("/tasks")
    public ResponseEntity<String> getAssignedUserTask() {
        return new ResponseEntity<>("WELCOME TO TASK SERVICE", HttpStatus.OK);
    }
}
