package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCreate {
    private String description;
    private Long requester;
}