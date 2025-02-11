package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserCreateRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdateRequest;

public interface UserService {
    UserResponse createUser(UserCreateRequest userDto);

    UserResponse getUser(Long id);

    UserResponse updateUser(Long id, UserUpdateRequest userDto);

    void deleteUser(Long id);
}