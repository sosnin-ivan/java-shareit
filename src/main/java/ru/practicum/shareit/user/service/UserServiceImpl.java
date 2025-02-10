package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.errors.ConflictException;
import ru.practicum.shareit.errors.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public UserDto createUser(UserDto userDto) {
        validateUserFields(userDto);
        checkEmail(userDto.getEmail());
        User createdUser = userStorage.createUser(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserDto(createdUser);
    }

    public UserDto getUser(Long id) {
        return UserMapper.mapToUserDto(checkUser(id));
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User preparedUser = prepareUser(id, userDto);
        return UserMapper.mapToUserDto(userStorage.updateUser(preparedUser));
    }

    public void deleteUser(Long id) {
        checkUser(id);
        userStorage.deleteUser(id);
    }

    private User prepareUser(Long id, UserDto userDto) {
        checkUser(id);
        UserDto oldUser = getUser(id);
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

    private void validateUserFields(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getName() == null) {
            throw new IllegalArgumentException("Email и имя должны быть заполнены");
        }
    }
}