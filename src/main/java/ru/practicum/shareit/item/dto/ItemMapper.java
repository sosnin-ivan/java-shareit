package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;

import java.util.List;

@Component
public class ItemMapper {
    public static ItemCreatedResponse mapToItemCreatedResponse(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null");
        }
        return ItemCreatedResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .build();
    }

    public static ItemResponse mapToItemResponse(Item item, List<Comment> comments) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null");
        }
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .comments(comments)
                .build();
    }

    public static Item mapToItem(ItemCreateRequest itemDto) {
        if (itemDto == null) {
            throw new IllegalArgumentException("ItemCreateRequest is null");
        }
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(itemDto.getOwner())
                .build();
    }

    public static Item mapToItem(ItemUpdateRequest itemDto) {
        if (itemDto == null) {
            throw new IllegalArgumentException("ItemUpdateRequest is null");
        }
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(itemDto.getOwner())
                .build();
    }
}