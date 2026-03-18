package com.maxim.taskmanager.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateDto {

    private String firstName;

    private String lastName;

    private int age;

    private String email;

    private String password; // может быть null (не обновляем)
}
