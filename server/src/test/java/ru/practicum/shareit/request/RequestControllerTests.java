package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.request.dto.RequestCreate;
import ru.practicum.shareit.request.dto.RequestResponse;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
public class RequestControllerTests {
    @Mock
    private RequestService requestService;

    @InjectMocks
    private RequestController controller;

    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private final Long userId = 1L;
    private RequestCreate requestCreate;
    private RequestResponse requestResponse;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();

        requestCreate = RequestCreate.builder().description("request").build();
        User requester = User.builder().id(userId).name("requester").email("requester@test.com").build();
        ItemResponse itemResponse = ItemResponse.builder().id(1L).name("item").description("item").available(true).build();
        requestResponse = RequestResponse.builder()
                .id(1L)
                .description("request")
                .requester(requester)
                .created(LocalDateTime.now())
                .items(List.of(itemResponse))
                .build();
    }

    @Test
    void createRequest() throws Exception {
        when(requestService.createRequest(any(RequestCreate.class), anyLong())).thenReturn(requestResponse);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestCreate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestResponse.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestResponse.getDescription())))
                .andExpect(jsonPath("$.created", notNullValue()));
    }

    @Test
    void getRequestsByUser() throws Exception {
        when(requestService.getRequestsByUser(anyLong())).thenReturn(List.of(requestResponse));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(requestResponse.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(requestResponse.getDescription())))
                .andExpect(jsonPath("$.[0].created", notNullValue()));
    }

    @Test
    void getAllIRequests() throws Exception {
        when(requestService.getAllRequests()).thenReturn(List.of(requestResponse));

        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(requestResponse.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(requestResponse.getDescription())))
                .andExpect(jsonPath("$.[0].created", notNullValue()));
    }

    @Test
    void getById() throws Exception {
        when(requestService.getRequest(anyLong())).thenReturn(requestResponse);

        mvc.perform(get("/requests/{id}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestResponse.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestResponse.getDescription())))
                .andExpect(jsonPath("$.created", notNullValue()));
    }
}
