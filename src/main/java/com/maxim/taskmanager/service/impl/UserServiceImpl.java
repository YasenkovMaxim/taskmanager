package com.maxim.taskmanager.service.impl;

import com.maxim.taskmanager.exception.UserAlreadyExistsException;
import com.maxim.taskmanager.exception.UserNotFoundException;
import com.maxim.taskmanager.model.dto.UserDto.UserCreateDto;
import com.maxim.taskmanager.model.dto.UserDto.UserMapper;
import com.maxim.taskmanager.model.dto.UserDto.UserResponseDto;
import com.maxim.taskmanager.model.dto.UserDto.UserUpdateDto;
import com.maxim.taskmanager.model.entity.User;
import com.maxim.taskmanager.repository.UserRepository;
import com.maxim.taskmanager.security.SecurityUtils;
import com.maxim.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;

    @Override
    public UserResponseDto getUserById(Integer id) {
        log.info("Поиск пользователя по id: {}", id);
        User currentUser = securityUtils.getCurrentUser();
        if (!securityUtils.isAdmin() && currentUser.getId() != id) {
            throw new RuntimeException("У вас нет прав на просмотр этого пользователя");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с id " + id + " не найден"));
        log.info("Найден пользователь с id: {}", id);
        return UserMapper.toResponseDto(user);
    }

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        log.info("Получение пользователей с пагинацией: страница {}, размер {}",
                pageable.getPageNumber(), pageable.getPageSize());
        if (!securityUtils.isAdmin()) {
            log.warn("Пользователь {} пытается получить список всех пользователей", securityUtils.getCurrentUser().getEmail());
            throw new RuntimeException("У вас нет прав на просмотр всех пользователей");
        }
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(UserMapper::toResponseDto);
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        log.info("Поиск пользователся по email: {}", email);
        User currentUser = securityUtils.getCurrentUser();
        if (!securityUtils.isAdmin() && !currentUser.getEmail().equals(email)) {
            throw new RuntimeException("У вас нет прав");
        }
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Пользователь с email " + email + " не найден"));
        log.info("пользователь найден с email: {}", email);
        return UserMapper.toResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto createUser(UserCreateDto userDto) {
        log.info("Создание пользователя с email: {}", userDto.getEmail());
        if (userRepository.existsByEmail(userDto.getEmail())) {
            log.warn("Попытка создать пользователя с существующим email: {}", userDto.getEmail());
            throw new UserAlreadyExistsException("Пользователь с email " + userDto.getEmail() + " уже существует");
        }
        User user = UserMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("Пользователь создан с id: {}", savedUser.getId());
        return UserMapper.toResponseDto(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        log.info("Удаление пользователя с id: {}", id);
        User currentUser = securityUtils.getCurrentUser();
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + id + " не существует"));
        if (!securityUtils.isAdmin() && currentUser.getId() != userToDelete.getId()) {
            log.warn("Пользователь {} пытается удалить пользователя {}", currentUser.getEmail(), userToDelete.getEmail());
            throw new RuntimeException("У вас нет прав на удаление этого пользователя");
        }
        userRepository.deleteById(id);
        log.info("Пользователь с id {} удалён", id);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Integer id, UserUpdateDto userDto) {
        log.info("Обновление пользователя с id: {}", id);
        User currentUser = securityUtils.getCurrentUser();
        if (!securityUtils.isAdmin() && currentUser.getId() != id) {
            log.warn("Пользователь {} пытается обновить пользователя {}", currentUser.getEmail(), id);
            throw new RuntimeException("У вас нет прав на обновление этого пользователя");
        }
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
