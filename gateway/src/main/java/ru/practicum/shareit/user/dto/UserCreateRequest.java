package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCreateRequest {
    @NotBlank(message = "email не может быть пустым")
    @Email(message = "некорректный email")
    private String email;

    @NotBlank(message = "имя не может быть пустым")
    private String name;
}