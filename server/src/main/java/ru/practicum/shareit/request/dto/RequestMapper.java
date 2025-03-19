package ru.practicum.shareit.request.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Component
public class RequestMapper {
    public static RequestResponse mapToItemRequestResponse(Request itemRequest) {
        if (itemRequest == null) {
            throw new IllegalArgumentException("ItemRequest is null");
        }
        return RequestResponse.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requester(itemRequest.getRequester())
                .created(itemRequest.getCreated())
                .build();
    }

    public static Request mapToItemRequest(RequestCreate requestDto, User requester) {
        if (requestDto == null) {
            throw new IllegalArgumentException("ItemRequestCreateRequest is null");
        }
        return Request.builder()
                .description(requestDto.getDescription())
                .requester(requester)
                .created(LocalDateTime.now())
                .build();
    }
}