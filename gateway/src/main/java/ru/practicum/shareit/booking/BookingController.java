package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.BookingState;

@Controller
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private static final String USER_ID_HEADER = "X-Sharer-User-Id";
	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> create(
			@RequestHeader(USER_ID_HEADER) long userId,
			@Valid @RequestBody BookingCreateRequest requestDto
	) {
		log.info("BookingController.bookItem: {}, by userId={}", userId, requestDto);
		return bookingClient.bookItem(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getById(
			@RequestHeader(USER_ID_HEADER) long userId,
			@PathVariable Long bookingId
	) {
		log.info("BookingController.getBooking: {}, by userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> update(
			@RequestHeader(USER_ID_HEADER) Long userId,
			@PathVariable Long bookingId,
			@RequestParam Boolean approved
	) {
		log.info("BookingController.updateBooking: {}, by userId={}, approved={}", bookingId, userId, approved);
		return bookingClient.updateBookingStatus(bookingId, userId, approved);
	}

	@GetMapping
	public ResponseEntity<Object> getBookingsByBooker(
			@RequestHeader(USER_ID_HEADER) Long userId,
			@RequestParam(defaultValue = "ALL") BookingState status
	) {
		log.info("BookingController.getBookingsByBooker: {}, by userId={}", status, userId);
		return bookingClient.getBookingsByBooker(userId, status);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingsByItemsOwner(
			@RequestHeader(USER_ID_HEADER) Long userId,
			@RequestParam(defaultValue = "ALL") BookingState status
	) {
		log.info("BookingController.getBookingsByItemsOwner: {}, by userId={}", status, userId);
		return bookingClient.getBookingsByItemsOwner(userId, status);
	}
}