package com.maxim.taskmanager.controller;

import com.maxim.taskmanager.model.dto.UserResponseDto;
import com.maxim.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Integer id) {
        // 1. Вызываем сервис (там вся логика + проверки)
        UserResponseDto user = userService.getUserById(id);

        // 2. Возвращаем ответ с кодом 200 OK
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}