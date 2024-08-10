package com.taskManager.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskManager.config.JwtProvider;
import com.taskManager.modal.User;
import com.taskManager.repository.UserRepository;

@Service
public class UserServiceImplimentation implements UserService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImplimentation.class);

    @Override
    public User getUserProfile(String jwt) {
        try {
            if (jwt == null || jwt.isEmpty()) {
                logger.warn("JWT token is null or empty");
                throw new IllegalArgumentException("JWT token is required");
            }

            String email = JwtProvider.getEmailFromJwtToken(jwt);

            if (email == null || email.isEmpty()) {
                logger.warn("Email extracted from JWT is null or empty");
                throw new IllegalArgumentException("Invalid JWT token");
            }

            User user = userRepository.findByEmail(email);

            if (user == null) {
                logger.info("No user found with email: {}", email);
            } else {
                logger.info("User profile retrieved for email: {}", email);
            }

            return user;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid JWT token", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving user profile", e);
            throw new RuntimeException("Error retrieving user profile", e);
        }
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void updateUser(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            existingUser.setFullName(user.getFullName());
            existingUser.setRole(user.getRole());
            userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUserProfiles() {
        try {
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                logger.info("No users found.");
            } else {
                logger.info("Retrieved all user profiles.");
            }
            return users;
        } catch (Exception e) {
            logger.error("Error retrieving all user profiles", e);
            throw new RuntimeException("Error retrieving all user profiles", e);
        }
    }
}
