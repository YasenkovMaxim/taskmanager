package com.maxim.taskmanager.controller;

import com.maxim.taskmanager.exception.UserNotFoundException;
import com.maxim.taskmanager.model.dto.UserDto.UserCreateDto;
import com.maxim.taskmanager.model.dto.UserDto.UserResponseDto;
import com.maxim.taskmanager.model.dto.UserDto.UserUpdateDto;
import com.maxim.taskmanager.model.entity.User;
import com.maxim.taskmanager.repository.UserRepository;
import com.maxim.taskmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Integer id) {
        log.info("GET /api/users/{} - поиск пользователя по id", id);
        UserResponseDto user = userService.getUserById(id);
        log.info("GET /api/users/{} - пользователь найден", id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        log.info("GET /api/users - получение списка всех пользователей");
        List<UserResponseDto> users = userService.getAllUsers();
        log.info("GET /api/users - найдено {} пользователей", users.size());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        log.info("GET /api/users/email/{} - поиск пользователя по email", email);
        UserResponseDto user = userService.getUserByEmail(email);
        log.info("GET /api/users/email/{} - пользователь найден", email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        log.info("POST /api/users - создание пользователя с email: {}", userCreateDto.getEmail());
        UserResponseDto user = userService.createUser(userCreateDto);
        log.info("POST /api/users - пользователь создан с id: {}", user.getId());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        log.info("DELETE /api/users/{} - удаление пользователя", id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = auth.getName();
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + id + " не найден"));
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(granted -> granted.getAuthority().equals("ROLE_ADMIN"));
        boolean isSelf = userToDelete.getEmail().equals(currentUserEmail);

        if (!isAdmin && !isSelf) {
            log.warn("Пользователь {} пытается удалить пользователя {}", currentUserEmail, userToDelete.getEmail());
            throw new RuntimeException("У вас нет прав на удаление этого пользователя");
        }
        userService.deleteUser(id);
        log.info("DELETE /api/users/{} - пользователь удалён", id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Integer id, @Valid @RequestBody UserUpdateDto userDto) {
        log.info("PUT /api/users/{} - обновление пользователя", id);
        UserResponseDto updatedUser = userService.updateUser(id, userDto);
        log.info("PUT /api/users/{} - пользователь обновлён", id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}