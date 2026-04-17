package com.maxim.taskmanager.controller;

import com.maxim.taskmanager.model.dto.userDto.UserCreateDto;
import com.maxim.taskmanager.model.dto.userDto.UserResponseDto;
import com.maxim.taskmanager.model.dto.userDto.UserUpdateDto;
import com.maxim.taskmanager.repository.UserRepository;
import com.maxim.taskmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Integer id) {
        log.info("GET /users/{} - поиск пользователя по id", id);
        UserResponseDto user = userService.getUserById(id);
        log.info("GET /users/{} - пользователь найден", id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("GET /users - страница: {}, размер: {}, сортировка: {} {}", page, size, sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserResponseDto> usersPage = userService.getAllUsers(pageable);
        log.info("GET /users - найдено {} пользователей, всего страниц: {}",
                usersPage.getNumberOfElements(), usersPage.getTotalPages());
        return ResponseEntity.ok(usersPage);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        log.info("GET /users/email/{} - поиск пользователя по email", email);
        UserResponseDto user = userService.getUserByEmail(email);
        log.info("GET /users/email/{} - пользователь найден", email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        log.info("POST /users - создание пользователя с email: {}", userCreateDto.getEmail());
        UserResponseDto user = userService.createUser(userCreateDto);
        log.info("POST /users - пользователь создан с id: {}", user.getId());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        log.info("DELETE /users/{} - удаление пользователя", id);
        userService.deleteUser(id);
        log.info("DELETE /users/{} - пользователь удалён", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Integer id, @Valid @RequestBody UserUpdateDto userDto) {
        log.info("PUT /users/{} - обновление пользователя", id);
        UserResponseDto updatedUser = userService.updateUser(id, userDto);
        log.info("PUT /users/{} - пользователь обновлён", id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}