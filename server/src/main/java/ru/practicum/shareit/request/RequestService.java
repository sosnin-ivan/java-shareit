package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.errors.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.request.dto.RequestCreate;
import ru.practicum.shareit.request.dto.RequestResponse;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestService {
    RequestRepository requestRepository;
    ItemRepository itemRepository;
    UserRepository userRepository;

    @Transactional
    public RequestResponse createRequest(RequestCreate itemRequestDto, Long userId) {
        User requester = findUser(userId);
        Request itemRequest = RequestMapper.mapToItemRequest(itemRequestDto, requester);
        return RequestMapper.mapToItemRequestResponse(requestRepository.save(itemRequest));
    }

    public List<RequestResponse> getRequestsByUser(Long userId) {
        User requester = findUser(userId);
        List<RequestResponse> requests = mapRequests(requestRepository.findByRequesterOrderByCreatedDesc(requester));
        setItems(requests);
        return requests;
    }

    public List<RequestResponse> getAllRequests() {
        List<RequestResponse> requests = mapRequests(requestRepository.findAllByOrderByCreatedDesc());
        setItems(requests);
        return requests;
    }

    public RequestResponse getRequest(Long requestId) {
        RequestResponse request = RequestMapper.mapToItemRequestResponse(findRequest(requestId));
        List<ItemResponse> items = itemRepository.findByRequest(requestId).stream()
                .map(ItemMapper::mapToItemResponseForRequest)
                .toList();
        request.setItems(items);
        return request;
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь c id %d не найден", userId)));
    }

    private Request findRequest(Long id) {
        return requestRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Запрос c id %d не найден", id)));
    }

    private List<RequestResponse> mapRequests(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::mapToItemRequestResponse)
                .toList();
    }

    private void setItems(List<RequestResponse> requests) {
        for (RequestResponse request : requests) {
            request.setItems(itemRepository.findByRequest(request.getId())
                    .stream()
                    .map(ItemMapper::mapToItemResponseForRequest)
                    .toList());
        }
    }
}