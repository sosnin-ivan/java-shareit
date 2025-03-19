package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.errors.ConflictException;
import ru.practicum.shareit.errors.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public BookingResponse createBooking(Long userId, BookingCreateRequest bookingRequest) {
        User booker = findUser(userId);
        Item item = findItem(bookingRequest.getItemId());
        if (!item.getAvailable()) {
            throw new IllegalArgumentException("Невозможно забронировать, т.к. вещь не доступна");
        }
        Booking createdBooking = bookingRepository.save(BookingMapper.mapToBooking(bookingRequest, item, booker));
        return BookingMapper.mapToBookingResponse(createdBooking);
    }

    public BookingResponse getBooking(Long userId, Long bookingId) {
        Booking booking = findBooking(bookingId);
        if (
                !booking.getBooker().getId().equals(userId) &&
                !booking.getItem().getOwner().equals(userId)
        ) {
            throw new ConflictException(String.format("У пользователя с id %d нет доступа", userId));
        }
        return BookingMapper.mapToBookingResponse(findBooking(bookingId));
    }

    @Transactional
    public BookingResponse updateBooking(Long userId, Long bookingId, Boolean approved) {
        Booking booking = findBooking(bookingId);
        if (!booking.getItem().getOwner().equals(userId)) {
            throw new IllegalArgumentException(String.format("Пользователь с id %d не является владельцем", userId));
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.mapToBookingResponse(booking);
    }

    public List<BookingResponse> getBookingsByBooker(Long userId, BookingState state) {
        return switch (state) {
            case ALL -> BookingMapper.mapToBookingResponse(
                    bookingRepository.findByBookerIdOrderByStartDesc(userId)
            );
            case CURRENT -> BookingMapper.mapToBookingResponse(
                    bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now())
            );
            case PAST -> BookingMapper.mapToBookingResponse(
                    bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now())
            );
            case FUTURE -> BookingMapper.mapToBookingResponse(
                    bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now())
            );
            case WAITING -> BookingMapper.mapToBookingResponse(
                    bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING)
            );
            case REJECTED -> BookingMapper.mapToBookingResponse(
                    bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED)
            );
        };
    }

    public List<BookingResponse> getBookingsByItemsOwner(Long userId, BookingState state) {
        if (itemRepository.findByOwner(userId).isEmpty()) {
            throw new NotFoundException(String.format("У пользователя с id = %d не найдено вещей", userId));
        }
        return switch (state) {
            case ALL -> BookingMapper.mapToBookingResponse(
                    bookingRepository.findByItemOwnerOrderByStartDesc(userId)
            );
            case CURRENT -> BookingMapper.mapToBookingResponse(
                    bookingRepository.findByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now())
            );
            case PAST -> BookingMapper.mapToBookingResponse(
                    bookingRepository.findByItemOwnerAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now())
            );
            case FUTURE -> BookingMapper.mapToBookingResponse(
                    bookingRepository.findByItemOwnerAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now())
            );
            case WAITING -> BookingMapper.mapToBookingResponse(
                    bookingRepository.findByItemOwnerAndStatusOrderByStartDesc(userId, BookingStatus.WAITING)
            );
            case REJECTED -> BookingMapper.mapToBookingResponse(
                    bookingRepository.findByItemOwnerAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED)
            );
        };
    }

    private Booking findBooking(Long id) {
        return bookingRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Бронирование c id %d не найдено", id)));
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь c id %d не найден", id)));
    }

    private Item findItem(Long id) {
        return itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Товар c id %d не найден", id)));
    }
}