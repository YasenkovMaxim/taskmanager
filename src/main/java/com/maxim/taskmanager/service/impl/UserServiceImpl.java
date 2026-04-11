package com.maxim.taskmanager.service.impl;

import com.maxim.taskmanager.exception.UserAlreadyExistsException;
import com.maxim.taskmanager.exception.UserNotFoundException;
import com.maxim.taskmanager.model.dto.UserDto.UserCreateDto;
import com.maxim.taskmanager.model.dto.UserDto.UserMapper;
import com.maxim.taskmanager.model.dto.UserDto.UserResponseDto;
import com.maxim.taskmanager.model.dto.UserDto.UserUpdateDto;
import com.maxim.taskmanager.model.entity.User;
import com.maxim.taskmanager.repository.UserRepository;
import com.maxim.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto getUserById(Integer id) {
        log.info("Поиск пользователя по id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с id " + id + " не найден"));
        log.info("Найден пользователь с id: {}", id);
        return UserMapper.toResponseDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        log.info("Получение списка всех пользователей");
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        log.info("Поиск пользователся по email: {}", email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Пользователь с email " + email + " не найден"));
        log.info("пользователь найден с email: {}", email);
        return UserMapper.toResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto createUser(UserCreateDto userDto) {
        log.info("Создание пользователя с email: {}", userDto.getEmail());
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с email " + userDto.getEmail() + " уже существует");
        } else {
            User savedUser = userRepository.save(UserMapper.toEntity(userDto));
            log.info("Пользователь создан с id: {}", savedUser.getId());
            return UserMapper.toResponseDto(savedUser);
        }
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        log.info("Попытка удаления пользователя с id: {}", id);
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            log.info("Пользователь с id {} удалён", id);
        } else{
            throw new UserNotFoundException("Пользователь с id: " + id + " не существует");
        }
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Integer id, UserUpdateDto userDto) {
        log.info("Обновление пользователя с id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + id + " не существует"));

        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new UserAlreadyExistsException("Пользователь с email " + userDto.getEmail() + " уже существует");
            }
        }
        UserMapper.updateEntity(userDto, user);
        User updatedUser = userRepository.save(user);
        log.info("Пользователь с id {} обновлён", id);
        return UserMapper.toResponseDto(updatedUser);
    }

    @Override
    public Integer getUserIdByEmail(String email) {
        log.info("Поиск id пользователя по email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с email " + email + " не найден"));
        return user.getId();
    }
}
