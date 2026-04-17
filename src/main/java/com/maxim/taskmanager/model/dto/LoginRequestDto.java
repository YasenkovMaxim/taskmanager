package com.maxim.taskmanager.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequestDto {

    @Schema(example = "ivan@mail.com", description = "Email пользователя")
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    @Schema(example = "12345678", description = "Пароль")
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, max = 30, message = "Пароль должен быть от 6 до 30 символов")
    private String password;
}
