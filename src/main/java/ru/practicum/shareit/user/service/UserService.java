package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUser(Long id);

    UserDto updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);
}