package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Comment;

import java.util.List;

@Data
@Builder
public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private Long request;
    private List<Comment> comments;
    private Booking nextBooking;
    private Booking lastBooking;
}