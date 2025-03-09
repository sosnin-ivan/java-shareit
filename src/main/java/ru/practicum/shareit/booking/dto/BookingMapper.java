package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingMapper {
    public static BookingResponse mapToBookingResponse(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking is null");
        }
        return BookingResponse.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }

    public static List<BookingResponse> mapToBookingResponse(Iterable<Booking> bookings) {
        if (bookings == null) {
            throw new IllegalArgumentException("Booking is null");
        }
        List<BookingResponse> mappedBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            mappedBookings.add(mapToBookingResponse(booking));
        }
        return mappedBookings;
    }

    public static Booking mapToBooking(BookingCreateRequest bookingDto, Item item, User booker) {
        if (bookingDto == null || item == null || booker == null) {
            throw new IllegalArgumentException("One of parameters is null");
        }
        return Booking.builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();
    }
}