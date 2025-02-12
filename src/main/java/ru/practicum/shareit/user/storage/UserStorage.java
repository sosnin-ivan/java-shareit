package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User createUser(User user);

    Optional<User> getUser(Long id);

    User updateUser(User user);

    void deleteUser(Long id);

    List<String> getEmails();
}