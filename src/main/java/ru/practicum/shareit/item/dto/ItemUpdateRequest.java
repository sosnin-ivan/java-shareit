package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemUpdateRequest {
    @Positive(message = "id не может быть отрицательным")
    Long id;

    private String name;
    private String description;
    private Boolean available;
    private Long owner;
}