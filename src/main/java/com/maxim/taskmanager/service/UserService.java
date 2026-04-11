package com.maxim.taskmanager.service;


import com.maxim.taskmanager.model.dto.UserDto.UserCreateDto;
import com.maxim.taskmanager.model.dto.UserDto.UserResponseDto;
import com.maxim.taskmanager.model.dto.UserDto.UserUpdateDto;

import java.util.List;

public interface UserService {

    UserResponseDto getUserById(Integer id);

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserByEmail(String email);

    UserResponseDto createUser(UserCreateDto userDto);

    void deleteUser(Integer id);

    UserResponseDto updateUser(Integer id, UserUpdateDto userDto);

    Integer getUserIdByEmail(String email);
}

