package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.dto.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long userId, LocalDateTime now, LocalDateTime end);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long userId, BookingStatus bookingStatus);

    List<Booking> findByItemOwnerOrderByStartDesc(Long userId);

    List<Booking> findByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long userId, LocalDateTime now, LocalDateTime end);

    List<Booking> findByItemOwnerAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findByItemOwnerAndStartIsAfterOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findByItemOwnerAndStatusOrderByStartDesc(Long userId, BookingStatus bookingStatus);

    List<Booking> findByBookerIdAndItemIdAndEndIsBeforeOrderByStartDesc(Long bookerId, Long itemId, LocalDateTime now);
}