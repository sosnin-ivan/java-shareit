package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestCreate;
import ru.practicum.shareit.request.dto.RequestResponse;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/requests")
public class RequestController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final RequestService requestService;

    @PostMapping
    public RequestResponse create(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestBody RequestCreate requestDto
    ) {
        log.info("RequestController.create: {}, by userId={}", requestDto, userId);
        return requestService.createRequest(requestDto, userId);
    }

    @GetMapping
    public List<RequestResponse> getRequestsByUser(
            @RequestHeader(USER_ID_HEADER) Long userId
    ) {
        log.info("RequestController.getRequestsByUser, by userId={}", userId);
        return requestService.getRequestsByUser(userId);
    }

    @GetMapping("/all")
    public List<RequestResponse> getAllIRequests() {
        log.info("RequestController.getAllRequests");
        return requestService.getAllRequests();
    }

    @GetMapping("/{requestId}")
    public RequestResponse getById(
            @PathVariable Long requestId
    ) {
        log.info("RequestController.getById, by id={}", requestId);
        return requestService.getRequest(requestId);
    }
}