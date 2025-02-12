package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemServiceImpl itemService;

    @PostMapping
    public ItemResponse create(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @Valid @RequestBody ItemCreateRequest itemDto
    ) {
        return itemService.createItem(userId, itemDto);
    }

    @GetMapping("/{id}")
    public ItemResponse getById(@PathVariable Long id) {
        return itemService.getItem(id);
    }

    @PatchMapping("/{id}")
    public ItemResponse update(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long id,
            @Valid @RequestBody ItemUpdateRequest itemDto
    ) {
        return itemService.updateItem(userId, id, itemDto);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long id
    ) {
        itemService.deleteItem(userId, id);
    }

    @GetMapping
    public List<ItemResponse> getUserItems(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemResponse> searchByParam(@RequestParam(value = "text") String query) {
        return itemService.searchItems(query);
    }
}