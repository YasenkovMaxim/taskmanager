package com.maxim.taskmanager.model.dto.userDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
}