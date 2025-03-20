package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestCreate;

@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestBody RequestCreate requestDto
    ) {
        log.info("RequestController.create: {}, by userId={}", requestDto, userId);
        return requestClient.create(requestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItemRequests(
            @RequestHeader(USER_ID_HEADER) Long userId
    ) {
        log.info("RequestController.getUserItemRequests, by userId={}", userId);
        return requestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(
            @RequestHeader(USER_ID_HEADER) Long userId
    ) {
        log.info("RequestController.getAllItemRequests, by userId={}", userId);
        return requestClient.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long requestId
    ) {
        log.info("RequestController.getById: {}, by userId={}", requestId, userId);
        return requestClient.getById(requestId, userId);
    }
}