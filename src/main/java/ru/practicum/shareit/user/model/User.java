package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    @Positive
    private Long id;

    @NotBlank(message = "email не может быть пустым")
    @Email(message = "некорректный email")
    private String email;

    @NotBlank(message = "name не может быть пустым")
    private String name;
}