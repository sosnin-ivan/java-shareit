package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {
    @Positive
    private Long id;

    @NotBlank(message = "название не может быть пустым")
    private String name;

    @NotBlank(message = "описание не может быть пустым")
    private String description;

    @NotBlank(message = "доступность не может быть пустым")
    private Boolean available;

    @NotBlank(message = "владелец не может отсутствовать")
    private Long owner;
}