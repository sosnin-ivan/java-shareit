package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingCreateRequest {
    @NotNull(message = "дата старта не может быть пустой")
    @Future
    private LocalDateTime start;

    @NotNull(message = "дата окончания не может быть пустой")
    @Future
    private LocalDateTime end;

    private Long itemId;
    private BookingStatus status;
}