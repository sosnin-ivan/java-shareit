package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Comment;

import java.util.List;

@Data
@Builder
public class ItemByUserResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private Booking nextBooking;
    private Booking lastBooking;
    private List<Comment> comments;
}