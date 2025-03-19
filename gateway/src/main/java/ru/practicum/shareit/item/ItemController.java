package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreateRequest;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
	private static final String USER_ID_HEADER = "X-Sharer-User-Id";
	private final ItemClient itemClient;

	@PostMapping
	public ResponseEntity<Object> create(
			@RequestHeader(USER_ID_HEADER) Long userId,
			@Valid @RequestBody ItemCreateRequest itemDto
	) {
		log.info("ItemController.create: {}", itemDto);
		return itemClient.createItem(userId, itemDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getById(
			@PathVariable Long id
	) {
		log.info("ItemController.getById: {}", id);
		return itemClient.getItem(id);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Object> update(
			@RequestHeader(USER_ID_HEADER) Long userId,
			@PathVariable Long id,
			@Valid @RequestBody ItemUpdateRequest itemDto
	) {
		log.info("ItemController.update: {}", itemDto);
		return itemClient.updateItem(userId, id, itemDto);
	}

	@DeleteMapping("/{id}")
	public void delete(
			@RequestHeader(USER_ID_HEADER) Long userId,
			@PathVariable Long id
	) {
		log.info("ItemController.delete: {}", id);
		itemClient.deleteItem(userId, id);
	}

	@GetMapping
	public ResponseEntity<Object> getUserItems(
			@RequestHeader(USER_ID_HEADER) Long userId
	) {
		log.info("ItemController.getUserItems: {}", userId);
		return itemClient.getUserItems(userId);
	}

	@GetMapping("/search")
	public ResponseEntity<Object> searchByParam(
			@RequestHeader(USER_ID_HEADER) Long userId,
			@RequestParam(value = "text") String query) {
		log.info("ItemController.searchByParam: {}", query);
		return itemClient.searchItems(userId, query);
	}

	@PostMapping("/{itemId}/comment")
	public ResponseEntity<Object> addComment(
			@RequestHeader(USER_ID_HEADER) Long userId,
			@PathVariable Long itemId,
			@Valid @RequestBody CommentCreateRequest commentDto
	) {
		log.info("ItemController.addComment: {}", commentDto);
		return itemClient.addComment(userId, itemId, commentDto);
	}
}
