package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateRequest {
    @Positive(message = "id не может быть отрицательным")
    Long id;

    @Email(message = "некорректный email")
    private String email;

    private String name;
}