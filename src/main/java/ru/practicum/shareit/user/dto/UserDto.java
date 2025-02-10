package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private long id;

    @Email(message = "некорректный email")
    private String email;

    private String name;
}