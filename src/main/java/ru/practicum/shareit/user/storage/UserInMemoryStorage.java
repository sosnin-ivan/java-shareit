package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UserInMemoryStorage implements UserStorage {
    private final Map<Long, User> users;
    private long currentId;

    public UserInMemoryStorage() {
        users = new HashMap<>();
        currentId = 0L;
    }

    public User createUser(User user) {
        user.setId(++currentId);
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    public Optional<User> getUser(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public User updateUser(User user) {
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    public void deleteUser(Long id) {
        users.remove(id);
    }

    public List<String> getEmails() {
        return users.values().stream().map(User::getEmail).toList();
    }
}