package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.errors.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.url=jdbc:h2:mem:shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTests {
    private User owner;
    private User requester;
    private Item item;
    private ItemCreateRequest itemCreateRequest;
    private ItemCreatedResponse itemCreatedResponse;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void beforeEach() {
        owner = userRepository.save(User.builder().name("owner").email("owner@test.ru").build());
        requester = userRepository.save(User.builder().name("requester").email("requester@test.ru").build());
        Request request = requestRepository.save(Request.builder().description("test").requester(requester).created(LocalDateTime.now()).build());
        itemCreateRequest = ItemCreateRequest.builder()
                .name("test")
                .description("test")
                .available(true)
                .owner(owner.getId())
                .requestId(request.getId())
                .build();
        item = itemRepository.save(ItemMapper.mapToItem(itemCreateRequest));
        itemCreatedResponse = ItemMapper.mapToItemCreatedResponse(item);
        Booking booking = Booking.builder()
                .item(item)
                .booker(requester)
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);
    }

    @Test
    void createItem() {
        itemCreatedResponse = itemService.createItem(owner.getId(), itemCreateRequest);
        assertNotNull(itemCreatedResponse);
        assertEquals(itemCreateRequest.getName(), itemCreatedResponse.getName());
        assertEquals(itemCreateRequest.getDescription(), itemCreatedResponse.getDescription());
        assertEquals(itemCreateRequest.getAvailable(), itemCreatedResponse.getAvailable());
    }

    @Test
    void getItem() {
        ItemResponse itemResponse = itemService.getItem(itemCreatedResponse.getId());
        assertNotNull(itemResponse);
        assertEquals(itemCreateRequest.getName(), itemResponse.getName());
        assertEquals(itemCreateRequest.getDescription(), itemResponse.getDescription());
        assertEquals(itemCreateRequest.getAvailable(), itemResponse.getAvailable());
    }

    @Test
    void updateItem() {
        ItemUpdateRequest itemUpdateRequest = ItemUpdateRequest.builder().available(false).build();
        ItemCreatedResponse itemResponse = itemService.updateItem(owner.getId(), item.getId(), itemUpdateRequest);
        assertNotNull(itemResponse);
        assertEquals(itemUpdateRequest.getAvailable(), itemResponse.getAvailable());
    }

    @Test
    void deleteItem() {
        itemService.deleteItem(owner.getId(), item.getId());
        assertThrows(NotFoundException.class, () -> itemService.getItem(itemCreatedResponse.getId()));
    }

    @Test
    void getUserItems() {
        List<ItemResponse> items = itemService.getUserItems(owner.getId());
        assertEquals(1, items.size());
        assertEquals(itemCreateRequest.getName(), items.getFirst().getName());
        assertEquals(itemCreateRequest.getDescription(), items.getFirst().getDescription());
        assertEquals(itemCreateRequest.getAvailable(), items.getFirst().getAvailable());
    }

    @Test
    void searchItems() {
        List<ItemCreatedResponse> items = itemService.searchItems("test");
        assertEquals(1, items.size());
        assertEquals(itemCreateRequest.getName(), items.getFirst().getName());
        assertEquals(itemCreateRequest.getDescription(), items.getFirst().getDescription());
        assertEquals(itemCreateRequest.getAvailable(), items.getFirst().getAvailable());
    }

    @Test
    void addComment() {
        CommentCreateRequest commentCreateRequest = CommentCreateRequest.builder().text("test").build();
        CommentResponse commentResponse = itemService.addComment(requester.getId(), itemCreatedResponse.getId(), commentCreateRequest);
        assertNotNull(commentResponse);
        assertEquals(commentResponse.getText(), "test");
        assertEquals(commentResponse.getAuthorName(), requester.getName());
    }

    @Test
    void createItemWithWrongRequestId() {
        itemCreateRequest = ItemCreateRequest.builder()
                .name("test")
                .description("test")
                .available(true)
                .owner(owner.getId())
                .requestId(999L)
                .build();
        assertThrows(NotFoundException.class, () -> itemService.createItem(owner.getId(), itemCreateRequest));
    }

    @Test
    void createItemWithWrongOwnerId() {
        assertThrows(NotFoundException.class, () -> itemService.createItem(999L, itemCreateRequest));
    }

    @Test
    void getItemWithWrongId() {
        assertThrows(NotFoundException.class, () -> itemService.getItem(999L));
    }

    @Test
    void deleteWithWrongOwner() {
        assertThrows(IllegalArgumentException.class, () -> itemService.deleteItem(999L, item.getId()));
    }
}