package com.maxim.taskmanager.model.dto.userDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateDto {

    @Size(min = 2, max = 12)
    private String firstName;

    @Size(min = 2, max = 12)
    private String lastName;

    @Min(12)
    @Max(110)
    private Integer age;

    @Email
    private String email;

    @Size(min = 6, max = 30)
    private String password;
}
