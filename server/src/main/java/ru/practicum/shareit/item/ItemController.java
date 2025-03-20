package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    public ItemCreatedResponse create(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestBody ItemCreateRequest itemDto
    ) {
        log.info("ItemController.create: {}", itemDto);
        return itemService.createItem(userId, itemDto);
    }

    @GetMapping("/{id}")
    public ItemResponse getById(
            @PathVariable Long id
    ) {
        log.info("ItemController.getById: {}", id);
        return itemService.getItem(id);
    }

    @PatchMapping("/{id}")
    public ItemCreatedResponse update(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long id,
            @RequestBody ItemUpdateRequest itemDto
    ) {
        log.info("ItemController.update: {}", itemDto);
        return itemService.updateItem(userId, id, itemDto);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long id
    ) {
        log.info("ItemController.delete: {}", id);
        itemService.deleteItem(userId, id);
    }

    @GetMapping
    public List<ItemResponse> getUserItems(
            @RequestHeader(USER_ID_HEADER) Long userId
    ) {
        log.info("ItemController.getUserItems: {}", userId);
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemCreatedResponse> searchByParam(
            @RequestParam(value = "text") String query
    ) {
        log.info("ItemController.searchByParam: {}", query);
        return itemService.searchItems(query);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponse addComment(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long itemId,
            @RequestBody CommentCreateRequest commentDto
    ) {
        log.info("ItemController.addComment: {}", commentDto);
        return itemService.addComment(userId, itemId, commentDto);
    }
}