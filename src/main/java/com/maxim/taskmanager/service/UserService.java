package com.maxim.taskmanager.service;


import com.maxim.taskmanager.model.dto.UserResponseDto;

public interface UserService {

    UserResponseDto getUserById(Integer id);
}

