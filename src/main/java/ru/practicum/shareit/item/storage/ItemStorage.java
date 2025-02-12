package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item createItem(Item item);

    Optional<Item> getItem(Long id);

    Item updateItem(Item item);

    void deleteItem(Long id);

    List<Item> getUserItems(Long userId);

    List<Item> searchItems(String query);
}