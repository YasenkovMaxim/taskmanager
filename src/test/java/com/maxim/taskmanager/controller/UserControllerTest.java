package com.maxim.taskmanager.controller;

import com.maxim.taskmanager.model.dto.UserDto.UserResponseDto;
import com.maxim.taskmanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getUserById_Success() {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(1);
        dto.setEmail("test@mail.com");
        when(userService.getUserById(1)).thenReturn(dto);
        ResponseEntity<UserResponseDto> response = userController.getUserById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
        assertEquals("test@mail.com", response.getBody().getEmail());
    }

    @Test
    void deleteUser_Success() {
        doNothing().when(userService).deleteUser(1);
        ResponseEntity<Void> response = userController.deleteUser(1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(1);
    }
}