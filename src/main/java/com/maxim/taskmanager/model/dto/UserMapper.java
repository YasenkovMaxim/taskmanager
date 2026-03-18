package com.maxim.taskmanager.model.dto;


import com.maxim.taskmanager.model.entity.User;

public class UserMapper {

    /**
     * Преобразует Entity User в DTO UserResponseDto
     */
    public static UserResponseDto toResponseDto(User user) {
        if (user == null) {
            return null;
        }
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        return dto;
    }
}