package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTests {
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController controller;

    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private final Long userId = 1L;
    private BookingCreateRequest bookingCreateRequest;
    private BookingResponse bookingResponse;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        mapper.registerModules(new JavaTimeModule());
        LocalDateTime dateTime = LocalDateTime.now();
        User booker = User.builder().id(2L).name("user").email("user@mail.ru").build();
        Item item = Item.builder().id(1L).name("item").description("description").available(true).owner(1L).request(null).build();

        bookingCreateRequest = BookingCreateRequest.builder()
                .start(dateTime)
                .end(dateTime.plusDays(1))
                .itemId(1L)
                .status(BookingStatus.WAITING)
                .build();
        bookingResponse = BookingResponse.builder()
                .id(1L)
                .start(dateTime)
                .end(dateTime.plusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();
    }

    @Test
    void createBooking() throws Exception {
        when(bookingService.createBooking(userId, bookingCreateRequest)).thenReturn(bookingResponse);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponse.getId()), Long.class))
                .andExpect(jsonPath("$.start", notNullValue()))
                .andExpect(jsonPath("$.end", notNullValue()))
                .andExpect(jsonPath("$.item.id", is(bookingResponse.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(bookingResponse.getItem().getName())))
                .andExpect(jsonPath("$.booker.id", is(bookingResponse.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(bookingResponse.getBooker().getName())))
                .andExpect(jsonPath("$.status", is(bookingResponse.getStatus().name())));
    }

    @Test
    void getById() throws Exception {
        when(bookingService.getBooking(userId, bookingResponse.getId())).thenReturn(bookingResponse);

        mvc.perform(get("/bookings/{id}", bookingResponse.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponse.getId()), Long.class))
                .andExpect(jsonPath("$.start", notNullValue()))
                .andExpect(jsonPath("$.end", notNullValue()))
                .andExpect(jsonPath("$.item.id", is(bookingResponse.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(bookingResponse.getItem().getName())))
                .andExpect(jsonPath("$.booker.id", is(bookingResponse.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(bookingResponse.getBooker().getName())))
                .andExpect(jsonPath("$.status", is(bookingResponse.getStatus().name())));
    }

    @Test
    void updateBooking() throws Exception {
        bookingResponse.setStatus(BookingStatus.APPROVED);
        when(bookingService.updateBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingResponse);

        mvc.perform(patch("/bookings/{id}", bookingResponse.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponse.getId()), Long.class))
                .andExpect(jsonPath("$.start", notNullValue()))
                .andExpect(jsonPath("$.end", notNullValue()))
                .andExpect(jsonPath("$.item.id", is(bookingResponse.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(bookingResponse.getItem().getName())))
                .andExpect(jsonPath("$.booker.id", is(bookingResponse.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(bookingResponse.getBooker().getName())))
                .andExpect(jsonPath("$.status", is(bookingResponse.getStatus().name())));
    }

    @Test
    void getBookingsByBooker() throws Exception {
        when(bookingService.getBookingsByBooker(anyLong(), any())).thenReturn(List.of(bookingResponse));

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(bookingResponse.getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", notNullValue()))
                .andExpect(jsonPath("$.[0].end", notNullValue()))
                .andExpect(jsonPath("$.[0].item.id", is(bookingResponse.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.[0].item.name", is(bookingResponse.getItem().getName())))
                .andExpect(jsonPath("$.[0].booker.id", is(bookingResponse.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[0].booker.name", is(bookingResponse.getBooker().getName())))
                .andExpect(jsonPath("$.[0].status", is(bookingResponse.getStatus().name())));
    }

    @Test
    void getBookingsByOwner() throws Exception {
        when(bookingService.getBookingsByItemsOwner(anyLong(), any())).thenReturn(List.of(bookingResponse));

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(bookingResponse.getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", notNullValue()))
                .andExpect(jsonPath("$.[0].end", notNullValue()))
                .andExpect(jsonPath("$.[0].item.id", is(bookingResponse.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.[0].item.name", is(bookingResponse.getItem().getName())))
                .andExpect(jsonPath("$.[0].booker.id", is(bookingResponse.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[0].booker.name", is(bookingResponse.getBooker().getName())))
                .andExpect(jsonPath("$.[0].status", is(bookingResponse.getStatus().name())));
    }
}