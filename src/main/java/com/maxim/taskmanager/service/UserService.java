package com.maxim.taskmanager.service;


import com.maxim.taskmanager.model.dto.UserCreateDto;
import com.maxim.taskmanager.model.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto getUserById(Integer id);

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserByEmail(String email);

    UserResponseDto createUser(UserCreateDto userDto);
}

