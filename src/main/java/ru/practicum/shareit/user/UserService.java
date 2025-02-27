package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.errors.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateRequest;
import ru.practicum.shareit.user.dto.UserUpdateRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserMapper;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserResponse createUser(UserCreateRequest userDto) {
        User createdUser = userRepository.save(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserResponse(createdUser);
    }

    @Transactional
    public UserResponse getUser(Long id) {
        return UserMapper.mapToUserResponse(findUser(id));
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest userDto) {
        User user = UserMapper.mapToUser(userDto);
        User oldUser = findUser(id);
        if (user.getEmail() == null) {
            user.setEmail(oldUser.getEmail());
        }
        if (user.getName() == null) {
            user.setName(oldUser.getName());
        }
        user.setId(id);

        userRepository.save(user);
        return UserMapper.mapToUserResponse(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь c id %d не найден", id)));
    }
}