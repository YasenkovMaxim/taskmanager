package com.maxim.taskmanager.service;


import com.maxim.taskmanager.model.dto.UserDto.UserCreateDto;
import com.maxim.taskmanager.model.dto.UserDto.UserResponseDto;
import com.maxim.taskmanager.model.dto.UserDto.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponseDto getUserById(Integer id);

    Page<UserResponseDto> getAllUsers(Pageable pageable);

    UserResponseDto getUserByEmail(String email);

    UserResponseDto createUser(UserCreateDto userDto);

    void deleteUser(Integer id);

    UserResponseDto updateUser(Integer id, UserUpdateDto userDto);

    Integer getUserIdByEmail(String email);
}

