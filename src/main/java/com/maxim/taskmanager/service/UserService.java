package com.maxim.taskmanager.service;


import com.maxim.taskmanager.model.dto.UserCreateDto;
import com.maxim.taskmanager.model.dto.UserResponseDto;
import com.maxim.taskmanager.model.dto.UserUpdateDto;

import java.util.List;

public interface UserService {

    UserResponseDto getUserById(Integer id);

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserByEmail(String email);

    UserResponseDto createUser(UserCreateDto userDto);

    void deleteUser(Integer id);

    UserResponseDto updateUser(Integer id, UserUpdateDto userDto);
}

