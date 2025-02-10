package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.errors.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemDto createItem(Long userId, ItemDto itemDto) {
        validateItemFields(itemDto);
        checkUser(userId);
        Item createdItem = ItemMapper.mapToItem(itemDto);
        createdItem.setOwner(userId);
        return ItemMapper.mapToItemDto(itemStorage.createItem(createdItem));
    }

    public ItemDto getItem(Long id) {
        return ItemMapper.mapToItemDto(checkItem(id));
    }

    public ItemDto updateItem(Long userId, Long id, ItemDto itemDto) {
        Item preparedItem = prepareItem(userId, id, itemDto);
        return ItemMapper.mapToItemDto(itemStorage.updateItem(preparedItem));
    }

    private Item prepareItem(Long userId, Long id, ItemDto itemDto) {
        checkUser(userId);
        checkItem(id);
        ItemDto oldItem = getItem(id);
        if (itemDto.getName() == null) {
            itemDto.setName(oldItem.getName());
        }
        if (itemDto.getDescription() == null) {
            itemDto.setDescription(oldItem.getDescription());
        }
        if (itemDto.getAvailable() == null) {
            itemDto.setAvailable(oldItem.getAvailable());
        }
        itemDto.setId(id);
        return ItemMapper.mapToItem(itemDto);
    }

    public void deleteItem(Long userId, Long id) {
        Item item = checkItem(id);
        checkOwner(userId, item);
        itemStorage.deleteItem(id);
    }

    public List<ItemDto> getUserItems(Long userId) {
        checkUser(userId);
        return itemStorage.getUserItems(userId).stream().map(ItemMapper::mapToItemDto).toList();
    }

    public List<ItemDto> searchItems(String query) {
        if (query.isBlank()) {
            return List.of();
        }
        return itemStorage.searchItems(query).stream().map(ItemMapper::mapToItemDto).toList();
    }

    private Item checkItem(Long id) {
        return itemStorage.getItem(id).orElseThrow(() ->
                new RuntimeException(String.format("Предмет c id %d не найден", id)));
    }

    private void checkUser(Long id) {
        if (userStorage.getUser(id).isEmpty()) {
            throw new NotFoundException(String.format("Пользователь c id %d не найден", id));
        }
    }

    private void checkOwner(Long userId, Item item) {
        if (!userId.equals(item.getOwner())) {
            throw new IllegalArgumentException(
                    String.format("Предмет c id %d не принадлежит пользователю с id %d", item.getId(), userId)
            );
        }
    }

    private void validateItemFields(ItemDto itemDto) {
        if (
                itemDto.getAvailable() == null ||
                itemDto.getName() == null || itemDto.getName().isBlank() ||
                itemDto.getDescription() == null || itemDto.getDescription().isBlank()
        ) {
            throw new IllegalArgumentException("Название, описание и доступность должны быть заполнены");
        }
    }
}