package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentCreateRequest {
    @NotBlank(message = "комментарий не может быть пустым")
    private String text;

    private LocalDateTime created;
}