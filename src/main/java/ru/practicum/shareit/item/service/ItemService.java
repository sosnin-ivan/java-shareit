package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto getItem(Long id);

    ItemDto updateItem(Long userId, Long id, ItemDto itemDto);

    void deleteItem(Long userId, Long id);

    List<ItemDto> getUserItems(Long userId);

    List<ItemDto> searchItems(String query);
}