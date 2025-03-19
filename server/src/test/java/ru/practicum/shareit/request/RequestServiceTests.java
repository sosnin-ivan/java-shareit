package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.errors.NotFoundException;
import ru.practicum.shareit.request.dto.RequestCreate;
import ru.practicum.shareit.request.dto.RequestResponse;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.url=jdbc:h2:mem:shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceTests {
    private Long userId;
    private Long requestId;
    private RequestCreate requestCreate;
    private RequestResponse requestResponse;

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userId = userRepository.save(User.builder().name("test").email("test@test.ru").build()).getId();
        requestCreate = RequestCreate.builder().description("test").requester(userId).build();
        requestResponse = requestService.createRequest(requestCreate, userId);
        requestId = requestResponse.getId();
    }

    @Test
    void createRequest() {
        assertNotNull(requestResponse);
        assertEquals(requestCreate.getDescription(), requestResponse.getDescription());
        assertEquals(requestCreate.getRequester(), requestResponse.getRequester().getId());
    }

    @Test
    void getRequestsByUser() {
        List<RequestResponse> requests = requestService.getRequestsByUser(userId);
        assertEquals(1, requests.size());
        assertEquals(requestResponse.getId(), requests.getFirst().getId());
        assertEquals(requestResponse.getDescription(), requests.getFirst().getDescription());
    }

    @Test
    void getAllRequests() {
        List<RequestResponse> requests = requestService.getAllRequests();
        assertEquals(1, requests.size());
        assertEquals(requestResponse.getId(), requests.getFirst().getId());
        assertEquals(requestResponse.getDescription(), requests.getFirst().getDescription());
    }

    @Test
    void getRequest() {
        RequestResponse request = requestService.getRequest(requestId);
        assertNotNull(request);
        assertEquals(requestResponse.getId(), request.getId());
        assertEquals(requestResponse.getDescription(), request.getDescription());
    }

    @Test
    void getRequestWithWrongId() {
        assertThrows(NotFoundException.class, () -> requestService.getRequest(999L));
    }

    @Test
    void getRequestsByUserWithWrongUser() {
        assertThrows(NotFoundException.class, () -> requestService.getRequestsByUser(999L));
    }
}
