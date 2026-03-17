package com.maxim.taskmanager.service;

import com.maxim.taskmanager.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(User user);

    Optional<User> getUserById(Integer id);

    Optional<User> getUserByEmail(String email);

    List<User> getAllUsers();

    User updateUser(Integer id, User userDetails);

    void deleteUser(Integer id);

    boolean existsByEmail(String email);
}

