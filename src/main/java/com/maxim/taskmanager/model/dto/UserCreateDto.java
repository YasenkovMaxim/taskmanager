package com.maxim.taskmanager.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserCreateDto {

    private String firstName;

    private String lastName;

    private int age;

    private String email;

    private String password;
}
