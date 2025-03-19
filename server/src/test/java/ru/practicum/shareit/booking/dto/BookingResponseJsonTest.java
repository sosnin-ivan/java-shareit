package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingResponseJsonTest {
    private final JacksonTester<BookingResponse> json;

    @Test
    void bookingResponse() throws Exception {
        LocalDateTime dateTime = LocalDateTime.now();
        User booker = User.builder()
                .id(1L)
                .email("test@test.ru")
                .name("test")
                .build();
        Item item = Item.builder()
                .id(1L)
                .name("test")
                .description("test")
                .available(true)
                .owner(1L)
                .build();
        BookingResponse bookingResponse = BookingResponse.builder()
                .id(1L)
                .start(dateTime)
                .end(dateTime.plusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.valueOf("APPROVED"))
                .build();
        JsonContent<BookingResponse> result = this.json.write(bookingResponse);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(bookingResponse.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.start").isNotNull();
        assertThat(result).extractingJsonPathStringValue("$.end").isNotNull();
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(item.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.item.owner").isEqualTo(item.getOwner().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(booker.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo(booker.getName());
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo(booker.getEmail());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(bookingResponse.getStatus().toString());
    }
}
