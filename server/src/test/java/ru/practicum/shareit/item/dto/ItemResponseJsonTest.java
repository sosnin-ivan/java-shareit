package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemResponseJsonTest {
    private final JacksonTester<ItemResponse> json;

    @Test
    void itemResponse() throws Exception {
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
        JsonContent<ItemResponse> result = this.json.write(itemResponse);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemResponse.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemResponse.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemResponse.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemResponse.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.owner").isEqualTo(itemResponse.getOwner().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.request").isEqualTo(itemResponse.getRequest().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.comments").isNull();
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking").isNull();
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking").isNull();
    }
}