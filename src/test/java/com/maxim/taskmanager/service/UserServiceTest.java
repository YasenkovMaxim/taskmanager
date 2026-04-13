package com.maxim.taskmanager.service;

import com.maxim.taskmanager.exception.UserAlreadyExistsException;
import com.maxim.taskmanager.exception.UserNotFoundException;
import com.maxim.taskmanager.model.dto.UserDto.UserCreateDto;
import com.maxim.taskmanager.model.dto.UserDto.UserResponseDto;
import com.maxim.taskmanager.model.entity.User;
import com.maxim.taskmanager.repository.UserRepository;
import com.maxim.taskmanager.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserCreateDto userCreateDto;
    private User user;

    @BeforeEach
    void setUp() {
        userCreateDto = new UserCreateDto();
        userCreateDto.setFirstName("Тест");
        userCreateDto.setLastName("Тестов");
        userCreateDto.setAge(25);
        userCreateDto.setEmail("test@mail.com");
        userCreateDto.setPassword("12345678");

        user = new User();
        user.setId(1);
        user.setFirstName("Тест");
        user.setLastName("Тестов");
        user.setAge(25);
        user.setEmail("test@mail.com");
        user.setPassword("encodedPassword");
    }

    @Test
    void createUser_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto result = userService.createUser(userCreateDto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("test@mail.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_EmailAlreadyExists_ThrowsException() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(userCreateDto);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserResponseDto result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("test@mail.com", result.getEmail());
    }

    @Test
    void getUserById_NotFound_ThrowsException() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(999);
        });
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.existsById(1)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1);

        assertDoesNotThrow(() -> userService.deleteUser(1));
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteUser_NotFound_ThrowsException() {
        when(userRepository.existsById(999)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(999);
        });
    }
}
