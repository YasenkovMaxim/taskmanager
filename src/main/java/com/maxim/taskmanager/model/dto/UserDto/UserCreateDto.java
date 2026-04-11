package com.maxim.taskmanager.model.dto.UserDto;

import com.maxim.taskmanager.model.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserCreateDto {

    @NotBlank
    @Size(min = 2, max = 12)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 12)
    private String lastName;

    @NotNull
    @Min(12)
    @Max(110)
    private Integer age;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 30)
    private String password;

    private Role role;
}
