package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.errors.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdateRequest;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.url=jdbc:h2:mem:shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceTests {
    private final UserService userService;

    private UserCreateRequest userCreateRequest;
    private UserResponse userResponse;

    @BeforeEach
    void beforeEach() {
        userCreateRequest = UserCreateRequest.builder().email("test@test.ru").name("test").build();
        userResponse = userService.createUser(userCreateRequest);
    }

    @Test
    void createUser() {
        assertNotNull(userResponse);
        assertEquals(userCreateRequest.getName(), userResponse.getName());
        assertEquals(userCreateRequest.getEmail(), userResponse.getEmail());
    }

    @Test
    void getUser() {
        UserResponse getUserResponse = userService.getUser(userResponse.getId());
        assertNotNull(getUserResponse);
        assertEquals(userResponse.getId(), getUserResponse.getId());
        assertEquals(userResponse.getName(), getUserResponse.getName());
        assertEquals(userResponse.getEmail(), getUserResponse.getEmail());
    }

    @Test
    void updateUser() {
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder().email("test2@test.ru").name("test2").build();
        UserResponse userUpdateResponse = userService.updateUser(userResponse.getId(), userUpdateRequest);
        assertNotNull(userUpdateResponse);
        assertEquals(userUpdateResponse.getId(), userResponse.getId());
        assertEquals(userUpdateRequest.getName(), userUpdateResponse.getName());
        assertEquals(userUpdateRequest.getEmail(), userUpdateResponse.getEmail());
    }

    @Test
    void updateUserWithBlankName() {
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder().email("test2@test.ru").build();
        UserResponse userUpdateResponse = userService.updateUser(userResponse.getId(), userUpdateRequest);
        assertNotNull(userUpdateResponse);
        assertEquals(userResponse.getName(), userUpdateResponse.getName());
        assertEquals(userUpdateRequest.getEmail(), userUpdateResponse.getEmail());
    }

    @Test
    void updateUserWithBlankEmail() {
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder().name("test2").build();
        UserResponse userUpdateResponse = userService.updateUser(userResponse.getId(), userUpdateRequest);
        assertNotNull(userUpdateResponse);
        assertEquals(userUpdateRequest.getName(), userUpdateResponse.getName());
        assertEquals(userResponse.getEmail(), userUpdateResponse.getEmail());
    }

    @Test
    void deleteUser() {
        userService.deleteUser(userResponse.getId());
        assertThrows(NotFoundException.class, () -> userService.getUser(userResponse.getId()));
    }
}