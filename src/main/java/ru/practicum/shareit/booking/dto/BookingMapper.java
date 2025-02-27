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
        List<BookingResponse> mappedBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            mappedBookings.add(mapToBookingResponse(booking));
        }
        return mappedBookings;
    }

    public static Booking mapToBooking(BookingCreateRequest bookingDto, Item item, User booker) {
        return Booking.builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();
    }
}