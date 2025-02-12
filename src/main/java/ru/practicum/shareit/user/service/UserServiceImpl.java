package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.errors.ConflictException;
import ru.practicum.shareit.errors.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateRequest;
import ru.practicum.shareit.user.dto.UserUpdateRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public UserResponse createUser(UserCreateRequest userDto) {
        checkEmail(userDto.getEmail());
        User createdUser = userStorage.createUser(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserResponse(createdUser);
    }

    public UserResponse getUser(Long id) {
        return UserMapper.mapToUserResponse(checkUser(id));
    }

    public UserResponse updateUser(Long id, UserUpdateRequest userDto) {
        User preparedUser = prepareUser(id, userDto);
        userStorage.updateUser(preparedUser);
        return UserMapper.mapToUserResponse(preparedUser);
    }

    public void deleteUser(Long id) {
        checkUser(id);
        userStorage.deleteUser(id);
    }

    private User prepareUser(Long id, UserUpdateRequest userDto) {
        checkUser(id);
        UserResponse oldUser = getUser(id);
        if (userDto.getEmail() == null) {
            userDto.setEmail(oldUser.getEmail());
        } else if (!oldUser.getEmail().equals(userDto.getEmail())) {
            checkEmail(userDto.getEmail());
        }
        if (userDto.getName() == null) {
            userDto.setName(oldUser.getName());
        }
        userDto.setId(id);
        return UserMapper.mapToUser(userDto);
    }

    private User checkUser(Long id) {
        return userStorage.getUser(id).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь c id %d не найден", id)));
    }

    private void checkEmail(String email) {
        if (userStorage.getEmails().contains(email)) {
            throw new ConflictException(String.format("Пользователь с email %s уже существует", email));
        }
    }
}