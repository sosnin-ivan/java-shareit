package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.errors.ConflictException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class UserInMemoryStorage implements UserStorage {
    private final Map<Long, User> users;
    private final Set<String> emailUniqSet;
    private long currentId;

    public UserInMemoryStorage() {
        users = new HashMap<>();
        emailUniqSet = new HashSet<>();
        currentId = 0L;
    }

    public User createUser(User user) {
        final String email = user.getEmail();
        checkEmailUniq(email);
        user.setId(++currentId);
        users.put(user.getId(), user);
        emailUniqSet.add(email);
        return user;
    }

    public Optional<User> getUser(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public User updateUser(User user) {
        final String email = user.getEmail();
        users.computeIfPresent(user.getId(), (id, u) -> {
            if (!email.equals(u.getEmail())) {
                checkEmailUniq(email);
                emailUniqSet.remove(u.getEmail());
                emailUniqSet.add(email);
            }
            return user;
        });
        return user;
    }

    public void deleteUser(Long id) {
        users.remove(id);
    }

    public List<String> getEmails() {
        return users.values().stream().map(User::getEmail).toList();
    }

    private void checkEmailUniq(String email) {
        if (emailUniqSet.contains(email)) {
            throw new ConflictException(String.format("Email: %s already exists", email));
        }
    }
}