package ru.practicum.shareit.user.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserResponseJsonTest {
    private final JacksonTester<UserResponse> json;

    @Test
    void userResponse() throws Exception {
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .email("test@test.ru")
                .name("test")
                .build();
        JsonContent<UserResponse> result = this.json.write(userResponse);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(userResponse.getEmail());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(userResponse.getName());
    }
}