package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestResponseJsonTest {
    private final JacksonTester<RequestResponse> json;

    @Test
    void requestResponse() throws Exception {
        ItemResponse itemResponse = ItemResponse.builder()
                .id(1L)
                .name("test")
                .description("test")
                .available(true)
                .owner(1L)
                .request(1L)
                .comments(null)
                .nextBooking(null)
                .lastBooking(null)
                .build();
        List<ItemResponse> items = List.of(itemResponse);
        RequestResponse requestResponse = RequestResponse.builder()
                .id(1L)
                .description("test")
                .requester(User.builder().id(1L).email("test@test.ru").name("test").build())
                .created(LocalDateTime.now())
                .items(items)
                .build();
        JsonContent<RequestResponse> result = this.json.write(requestResponse);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(requestResponse.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(requestResponse.getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.requester.id").isEqualTo(requestResponse.getRequester().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.requester.email").isEqualTo(requestResponse.getRequester().getEmail());
        assertThat(result).extractingJsonPathStringValue("$.requester.name").isEqualTo(requestResponse.getRequester().getName());
        assertThat(result).extractingJsonPathStringValue("$.created").isNotNull();
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(itemResponse.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo(itemResponse.getName());
        assertThat(result).extractingJsonPathStringValue("$.items[0].description").isEqualTo(itemResponse.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.items[0].available").isEqualTo(itemResponse.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].owner").isEqualTo(itemResponse.getOwner().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].request").isEqualTo(itemResponse.getRequest().intValue());
        assertThat(result).extractingJsonPathStringValue("$.items[0].lastBooking").isNull();
        assertThat(result).extractingJsonPathStringValue("$.items[0].nextBooking").isNull();
        assertThat(result).extractingJsonPathStringValue("$.items[0].comments").isNull();
    }
}
