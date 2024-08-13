package com.taskManager.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.taskManager.modal.UserDTO;

@FeignClient(name="USER-SERVICE", url="http://localhost:5001")
public interface UserService {

    @GetMapping("/api/users/profile")
    public UserDTO getUserProfile(@RequestHeader("Authorization") String jwt);


}
