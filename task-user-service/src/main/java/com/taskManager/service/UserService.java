package com.taskManager.service;

import java.util.List;

import com.taskManager.modal.User;

public interface UserService {

    User getUserProfile(String jwt);

    User getUserById(Long id);

    User getUserByEmail(String email);

    void updateUser(User user);

    User createUser(User user);

    void deleteUser(Long id);

    List<User> getAllUserProfiles();
}
