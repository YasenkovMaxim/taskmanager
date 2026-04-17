package com.maxim.taskmanager.model.dto.UserDto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(example = "Иван", description = "Имя пользователя")
    @NotBlank
    @Size(min = 2, max = 12)
    private String firstName;

    @Schema(example = "Петров", description = "Фамилия пользователя")
    @NotBlank
    @Size(min = 2, max = 12)
    private String lastName;

    @Schema(example = "25", description = "Возраст")
    @NotNull
    @Min(12)
    @Max(110)
    private Integer age;

    @Schema(example = "ivan@mail.com", description = "Email пользователя")
    @NotBlank
    @Email
    private String email;

    @Schema(example = "12345678", description = "Пароль (минимум 6 символов)")
    @NotBlank
    @Size(min = 6, max = 30)
    private String password;
}
