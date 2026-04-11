package com.maxim.taskmanager.controller;

import com.maxim.taskmanager.model.dto.AuthResponseDto;
import com.maxim.taskmanager.model.dto.LoginRequestDto;
import com.maxim.taskmanager.model.dto.UserDto.UserCreateDto;
import com.maxim.taskmanager.model.dto.UserDto.UserResponseDto;
import com.maxim.taskmanager.service.UserService;
import com.maxim.taskmanager.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserCreateDto userDto) {
        log.info("POST /api/auth/register - регистрация пользователя с email: {}", userDto.getEmail());
        UserResponseDto createdUser = userService.createUser(userDto);
        log.info("POST /api/auth/register - пользователь зарегистрирован с id: {}", createdUser.getId());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        log.info("POST /api/auth/login - попытка входа для email: {}", loginRequest.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        Integer userId = userService.getUserIdByEmail(loginRequest.getEmail());
        String token = jwtService.generateToken(loginRequest.getEmail(), userId);
        log.info("POST /api/auth/login - пользователь {} успешно вошёл", loginRequest.getEmail());
        return ResponseEntity.ok(new AuthResponseDto(token, "Bearer", System.currentTimeMillis() + 86400000));
    }
}
