package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTests {
    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController controller;

    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private final Long userId = 1L;
    private ItemCreateRequest itemCreateRequest;
    private ItemCreatedResponse itemCreatedResponse;
    private ItemResponse itemResponse;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();

        itemCreateRequest = ItemCreateRequest.builder().name("item").description("item").available(true).build();
        itemCreatedResponse = ItemCreatedResponse.builder().id(1L).name("item").description("item").available(true).build();
        itemResponse = ItemResponse.builder().id(1L).name("item").description("item").available(true).build();
    }

    @Test
    void createItem() throws Exception {
        when(itemService.createItem(anyLong(), any(ItemCreateRequest.class))).thenReturn(itemCreatedResponse);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemCreatedResponse.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemCreatedResponse.getName())))
                .andExpect(jsonPath("$.description", is(itemCreatedResponse.getDescription())))
                .andExpect(jsonPath("$.available", is(itemCreatedResponse.getAvailable())));
    }

    @Test
    void getById() throws Exception {
        when(itemService.getItem(anyLong())).thenReturn(itemResponse);

        mvc.perform(get("/items/{id}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemResponse.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemResponse.getName())))
                .andExpect(jsonPath("$.description", is(itemResponse.getDescription())))
                .andExpect(jsonPath("$.available", is(itemResponse.getAvailable())));
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any(ItemUpdateRequest.class))).thenReturn(itemCreatedResponse);

        mvc.perform(patch("/items/{id}", 1)
                        .content(mapper.writeValueAsString(ItemUpdateRequest.builder().name("item").description("item").available(true).build()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemCreatedResponse.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemCreatedResponse.getName())))
                .andExpect(jsonPath("$.description", is(itemCreatedResponse.getDescription())))
                .andExpect(jsonPath("$.available", is(itemCreatedResponse.getAvailable())));
    }

    @Test
    void deleteItem() throws Exception {
        mvc.perform(delete("/items/{id}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void getUserItems() throws Exception {
        when(itemService.getUserItems(anyLong())).thenReturn(List.of(itemResponse));

        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(itemResponse.getId().intValue())))
                .andExpect(jsonPath("$.[0].name", is(itemResponse.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemResponse.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(itemResponse.getAvailable())));
    }

    @Test
    void searchByParam() throws Exception {
        when(itemService.searchItems(anyString())).thenReturn(List.of(itemCreatedResponse));

        mvc.perform(get("/items/search")
                        .param("text", "text")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(itemCreatedResponse.getId().intValue())))
                .andExpect(jsonPath("$.[0].name", is(itemCreatedResponse.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemCreatedResponse.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(itemCreatedResponse.getAvailable())));
    }

    @Test
    void addComment() throws Exception {
        CommentResponse commentResponse = CommentResponse.builder().id(1L).text("text").authorName("authorName").created(LocalDateTime.now()).build();
        when(itemService.addComment(anyLong(), anyLong(), any(CommentCreateRequest.class))).thenReturn(commentResponse);

        mvc.perform(post("/items/{itemId}/comment", 1)
                        .content(mapper.writeValueAsString(CommentCreateRequest.builder().text("text").build()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentResponse.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentResponse.getText())))
                .andExpect(jsonPath("$.authorName", is(commentResponse.getAuthorName())));
    }
}