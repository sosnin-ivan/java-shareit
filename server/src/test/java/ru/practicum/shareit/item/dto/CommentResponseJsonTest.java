package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentResponseJsonTest {
    private final JacksonTester<CommentResponse> json;

    @Test
    void commentResponse() throws Exception {
        LocalDateTime dateTime = LocalDateTime.now();
        CommentResponse commentResponse = CommentResponse.builder()
                .id(1L)
                .text("text")
                .authorName("authorName")
                .created(dateTime)
                .build();
        JsonContent<CommentResponse> result = this.json.write(commentResponse);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(commentResponse.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(commentResponse.getText());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(commentResponse.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.created").isNotNull();
    }
}