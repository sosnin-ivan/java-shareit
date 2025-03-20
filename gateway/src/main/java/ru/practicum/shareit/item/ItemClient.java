package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentCreateRequest;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(long userId, ItemCreateRequest requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getItem(long itemId) {
        return get("/" + itemId);
    }

    public ResponseEntity<Object> updateItem(long itemId, long userId, ItemUpdateRequest requestDto) {
        return patch("/" + itemId, userId, requestDto);
    }

    public void deleteItem(Long userId, Long itemId) {
        delete("/" + userId, itemId);
    }

    public ResponseEntity<Object> getUserItems(long userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> searchItems(long userId, String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> addComment(long userId, long itemId, CommentCreateRequest commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}