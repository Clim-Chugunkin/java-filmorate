package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.update(user);
    }

    public User getUserById(Long id) {
        return userStorage
                .getUsers()
                .stream()
                .filter((it) -> Objects.equals(it.getId(), id))
                .findFirst()
                .orElseThrow(() -> new ConditionsNotMetException("нет такого пользователя"));
    }

    public User addFriend(Long userId, Long friendId) {
        //проверяем есть ли пользователь
        User user = getUserById(userId);
        //проверяем есть ли друг
        User friend = getUserById(friendId);
        //нельзя добавлять  в друзья самого себя
        if (Objects.equals(userId, friendId)) {
            return user;
        }
        user.addFriend(friendId);
        friend.addFriend(userId);
        log.info("пользователь {} добавил в друзья {}", user.getName(), friend.getName());
        return user;
    }

    public List<User> getFriends(Long userId) {
        User user = getUserById(userId);
        return user.getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public User removeFriend(Long userId, Long friendId) {
        //проверяем есть ли пользователь
        User user = getUserById(userId);
        //проверяем есть ли друг
        User friend = getUserById(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
        log.info("пользователь {} удалил из друзей {}", user.getName(), friend.getName());
        return user;
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        //проверяем есть ли пользователь
        User user = getUserById(userId);
        //проверяем есть ли друг
        User friend = getUserById(otherId);
        return user.getFriends()
                .stream()
                .filter(friend.getFriends()::contains)
                .map(this::getUserById)
                .collect(Collectors.toList());
    }
}
