package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;

import java.util.List;

public interface ItemService {
    ItemResponse createItem(Long userId, ItemCreateRequest itemDto);

    ItemResponse getItem(Long id);

    ItemResponse updateItem(Long userId, Long id, ItemUpdateRequest itemDto);

    void deleteItem(Long userId, Long id);

    List<ItemResponse> getUserItems(Long userId);

    List<ItemResponse> searchItems(String query);
}