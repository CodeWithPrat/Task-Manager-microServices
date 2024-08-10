package com.taskManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskManager.modal.User;


public interface  UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);
    
}
