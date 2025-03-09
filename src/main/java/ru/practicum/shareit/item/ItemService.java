package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.errors.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ItemCreatedResponse createItem(Long userId, ItemCreateRequest itemDto) {
        findUser(userId);
        Item createdItem = ItemMapper.mapToItem(itemDto);
        createdItem.setOwner(userId);
        return ItemMapper.mapToItemCreatedResponse(itemRepository.save(createdItem));
    }

    public ItemResponse getItem(Long id) {
        Item item = findItem(id);
        return addBookingsAndComments(item);
    }

    @Transactional
    public ItemCreatedResponse updateItem(Long userId, Long id, ItemUpdateRequest itemDto) {
        Item preparedItem = prepareItem(userId, id, itemDto);
        return ItemMapper.mapToItemCreatedResponse(itemRepository.save(preparedItem));
    }

    @Transactional
    public void deleteItem(Long userId, Long id) {
        Item item = findItem(id);
        checkOwner(userId, item);
        itemRepository.deleteById(id);
    }

    public List<ItemResponse> getUserItems(Long userId) {
        findUser(userId);
        List<Item> items = itemRepository.findByOwner(userId);
        List<ItemResponse> mappedItems = new ArrayList<>();
        items.forEach(item -> mappedItems.add(addBookingsAndComments(item)));
        return mappedItems;
    }

    public List<ItemCreatedResponse> searchItems(String query) {
        if (query.isBlank()) {
            return List.of();
        }
        return itemRepository.search(query).stream().map(ItemMapper::mapToItemCreatedResponse).toList();
    }

    @Transactional
    public CommentResponse addComment(Long userId, Long id, CommentCreateRequest commentDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException(String.format("Пользователь c id %d не найден", userId)));
        Item item = findItem(id);
        if (bookingRepository.findByBookerIdAndItemIdAndEndIsBeforeOrderByStartDesc(userId, item.getId(), LocalDateTime.now()).isEmpty()) {
            throw new IllegalArgumentException("Оставить отзыв можно только после того, как закончилось бронирование");
        }
        Comment createdComment = commentRepository.save(CommentMapper.mapToComment(commentDto, user, item));
        return CommentMapper.mapToCommentResponse(createdComment);
    }

    private Item prepareItem(Long userId, Long id, ItemUpdateRequest itemDto) {
        findUser(userId);
        findItem(id);
        ItemResponse oldItem = getItem(id);
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

    private ItemResponse addBookingsAndComments(Item item) {
        List<Booking> bookings = bookingRepository.findByBookerIdAndItemIdAndEndIsBeforeOrderByStartDesc(item.getOwner(), item.getId(), LocalDateTime.now());
        List<Comment> comments = commentRepository.findByItemId(item.getId());
        ItemResponse mappedItem = ItemMapper.mapToItemResponse(item, comments);
        if (!bookings.isEmpty()) {
            mappedItem.setLastBooking(bookings.getLast());
            mappedItem.setNextBooking(bookings.get(bookings.size() - 2));
        }
        return mappedItem;
    }

    private Item findItem(Long id) {
        return itemRepository.findById(id).orElseThrow(() ->
                new RuntimeException(String.format("Предмет c id %d не найден", id)));
    }

    private void findUser(Long id) {
        if (userRepository.findById(id).isEmpty()) {
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
}