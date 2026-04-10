package com.maxim.taskmanager.controller;

import com.maxim.taskmanager.model.dto.UserCreateDto;
import com.maxim.taskmanager.model.dto.UserResponseDto;
import com.maxim.taskmanager.model.dto.UserUpdateDto;
import com.maxim.taskmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        userService.deleteUser(id);
        log.info("DELETE /api/users/{} - пользователь удалён", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Integer id, @Valid @RequestBody UserUpdateDto userDto) {
        log.info("PUT /api/users/{} - обновление пользователя", id);
        UserResponseDto updatedUser = userService.updateUser(id, userDto);
        log.info("PUT /api/users/{} - пользователь обновлён", id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}