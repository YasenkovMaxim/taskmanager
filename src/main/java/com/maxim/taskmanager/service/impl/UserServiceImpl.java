package com.maxim.taskmanager.service.impl;

import com.maxim.taskmanager.exception.UserAlreadyExistsException;
import com.maxim.taskmanager.exception.UserNotFoundException;
import com.maxim.taskmanager.model.dto.UserCreateDto;
import com.maxim.taskmanager.model.dto.UserMapper;
import com.maxim.taskmanager.model.dto.UserResponseDto;
import com.maxim.taskmanager.model.entity.User;
import com.maxim.taskmanager.repository.UserRepository;
import com.maxim.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto getUserById(Integer id) {
        // 1. Ищем пользователя в базе данных
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с id " + id + " не найден"));

        // 2. Преобразуем Entity в DTO (без пароля)
        return UserMapper.toResponseDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Пользователь с email " + email + " не найден"));
        return UserMapper.toResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto createUser(UserCreateDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с email " + userDto.getEmail() + " уже существует");
        } else {
            User savedUser = userRepository.save(UserMapper.toEntity(userDto));
            return UserMapper.toResponseDto(savedUser);
        }
    }

}
