package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RequestResponse {
    private Long id;
    private String description;
    private User requester;
    private LocalDateTime created;
    private List<ItemResponse> items;
}