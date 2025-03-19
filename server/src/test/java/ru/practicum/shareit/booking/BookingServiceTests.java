package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.errors.ConflictException;
import ru.practicum.shareit.errors.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.url=jdbc:h2:mem:shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTests {
    private BookingCreateRequest bookingCreateRequest;
    private User user;
    private Item item;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(User.builder().name("test").email("test@test.com").build());
        item = itemRepository.save(Item.builder().name("test").description("test").available(true).owner(user.getId()).build());
        bookingCreateRequest = BookingCreateRequest.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .itemId(item.getId())
                .build();
    }

    @Test
    void createBooking() {
        BookingResponse bookingResponse = bookingService.createBooking(user.getId(), bookingCreateRequest);
        assertNotNull(bookingResponse);
        assertEquals(bookingCreateRequest.getStart(), bookingResponse.getStart());
        assertEquals(bookingCreateRequest.getEnd(), bookingResponse.getEnd());
        assertEquals(bookingCreateRequest.getItemId(), bookingResponse.getItem().getId());
    }

    @Test
    void getBooking() {
        BookingResponse createdBookingResponse = bookingService.createBooking(user.getId(), bookingCreateRequest);
        BookingResponse fetchedBookingResponse = bookingService.getBooking(user.getId(), createdBookingResponse.getId());
        assertNotNull(fetchedBookingResponse);
        assertEquals(bookingCreateRequest.getItemId(), fetchedBookingResponse.getItem().getId());
    }

    @Test
    void updateBooking() {
        BookingResponse bookingResponse = bookingService.createBooking(user.getId(), bookingCreateRequest);
        BookingResponse fetchedBookingResponse = bookingService.updateBooking(user.getId(), bookingResponse.getId(), true);
        assertNotNull(fetchedBookingResponse);
        assertEquals(BookingStatus.APPROVED, fetchedBookingResponse.getStatus());
    }

    @Test
    void getBookingsByBooker() {
        BookingResponse bookingResponse = bookingService.createBooking(user.getId(), bookingCreateRequest);
        List<BookingResponse> fetchedBookingResponses = bookingService.getBookingsByBooker(user.getId(), BookingState.ALL);
        assertNotNull(fetchedBookingResponses);
        assertEquals(bookingResponse.getId(), fetchedBookingResponses.getFirst().getId());
    }

    @Test
    void getBookingsByItemsOwner() {
        BookingResponse bookingResponse = bookingService.createBooking(user.getId(), bookingCreateRequest);
        List<BookingResponse> fetchedBookingResponses = bookingService.getBookingsByItemsOwner(user.getId(), BookingState.ALL);
        assertNotNull(fetchedBookingResponses);
        assertEquals(bookingResponse.getId(), fetchedBookingResponses.getFirst().getId());
    }

    @Test
    void createBookingWithWrongUserId() {
        assertThrows(NotFoundException.class, () -> bookingService.createBooking(999L, bookingCreateRequest));
    }

    @Test
    void createBookingWithWrongItemId() {
        bookingCreateRequest = BookingCreateRequest.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .itemId(999L)
                .build();
        assertThrows(NotFoundException.class, () -> bookingService.createBooking(user.getId(), bookingCreateRequest));
    }

    @Test
    void getBookingWithWrongBookingId() {
        assertThrows(NotFoundException.class, () -> bookingService.getBooking(user.getId(), 999L));
    }

    @Test
    void getBookingWithWrongUserId() {
        BookingResponse bookingResponse = bookingService.createBooking(user.getId(), bookingCreateRequest);
        assertThrows(ConflictException.class, () -> bookingService.getBooking(999L, bookingResponse.getId()));
    }

    @Test
    void createBookingWithUnavailableItem() {
        item.setAvailable(false);
        assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(user.getId(), bookingCreateRequest));
    }
}