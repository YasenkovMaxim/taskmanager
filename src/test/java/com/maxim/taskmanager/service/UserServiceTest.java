package com.maxim.taskmanager.service;

import com.maxim.taskmanager.exception.UserAlreadyExistsException;
import com.maxim.taskmanager.exception.UserNotFoundException;
import com.maxim.taskmanager.model.dto.userDto.UserCreateDto;
import com.maxim.taskmanager.model.dto.userDto.UserResponseDto;
import com.maxim.taskmanager.model.entity.Role;
import com.maxim.taskmanager.model.entity.User;
import com.maxim.taskmanager.repository.UserRepository;
import com.maxim.taskmanager.security.SecurityUtils;
import com.maxim.taskmanager.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private UserServiceImpl userService;

    private UserCreateDto userCreateDto;
    private User user;
    private User currentUser;

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
        user.setRole(Role.USER);

        currentUser = new User();
        currentUser.setId(1);
        currentUser.setEmail("test@mail.com");
        currentUser.setRole(Role.USER);
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
        when(securityUtils.getCurrentUser()).thenReturn(currentUser);
        when(securityUtils.isAdmin()).thenReturn(false);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserResponseDto result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("test@mail.com", result.getEmail());
    }

    @Test
    void getUserById_UserNotFound_ThrowsException() {
        // Пользователь ищет себя (id=1), но в БД его нет
        when(securityUtils.getCurrentUser()).thenReturn(currentUser);
        when(securityUtils.isAdmin()).thenReturn(false);
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(1);
        });
    }

    @Test
    void getUserById_OtherUser_ThrowsAccessDenied() {
        // Пользователь пытается посмотреть чужого (id=2)
        when(securityUtils.getCurrentUser()).thenReturn(currentUser);
        when(securityUtils.isAdmin()).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            userService.getUserById(2);
        });

        verify(userRepository, never()).findById(2);
    }

    @Test
    void deleteUser_Success() {
        when(securityUtils.getCurrentUser()).thenReturn(currentUser);
        when(securityUtils.isAdmin()).thenReturn(false);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1);

        assertDoesNotThrow(() -> userService.deleteUser(1));
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteUser_NotFound_ThrowsException() {
        when(securityUtils.getCurrentUser()).thenReturn(currentUser);
        when(securityUtils.isAdmin()).thenReturn(false);
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(999);
        });
    }
}