package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class ItemInMemoryStorage implements ItemStorage {
    private final Map<Long, Item> items;
    private long currentId;

    public ItemInMemoryStorage() {
        items = new HashMap<>();
        currentId = 0L;
    }

    public Item createItem(Item item) {
        item.setId(++currentId);
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    public Optional<Item> getItem(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    public void deleteItem(Long id) {
        items.remove(id);
    }

    public List<Item> getUserItems(Long userId) {
        return items.values().stream()
                .filter(item -> userId.equals(item.getOwner()))
                .toList();
    }

    public List<Item> searchItems(String query) {
        return items.values().stream()
                .filter(item ->
                        item.getName().toUpperCase().contains(query.toUpperCase()) ||
                        item.getDescription().toUpperCase().contains(query.toUpperCase())
                )
                .filter(Item::getAvailable)
                .toList();
    }
}