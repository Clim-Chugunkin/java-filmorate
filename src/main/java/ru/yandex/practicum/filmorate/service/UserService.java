package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DAL.FriendsRepository;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    @Qualifier("userRepository")
    private UserStorage userStorage;
    private final FriendsRepository friendsRepository;

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
        return userStorage.getUserById(id);

    }

    public User addFriend(Long userId, Long friendId) {
        //проверяем есть ли пользователь
        User user = userStorage.getUserById(userId);
        //проверяем есть ли друг
        User friend = userStorage.getUserById(friendId);
        //нельзя добавлять  в друзья самого себя
        if (Objects.equals(userId, friendId)) {
            return user;
        }
        friendsRepository.addFriend(userId, friendId);
        log.info("пользователь {} добавил в друзья {}", user.getName(), friend.getName());
        return user;
    }

    public List<Friends> getFriends(Long userId) {
        User user = getUserById(userId);
        return friendsRepository.getUserFriends(userId);
    }

    public User removeFriend(Long userId, Long friendId) {
        //проверяем есть ли пользователь
        User user = userStorage.getUserById(userId);
        //проверяем есть ли друг
        User friend = userStorage.getUserById(friendId);
        int count = friendsRepository.deleteFriend(userId, friendId);
        if (count == 0) {
            log.info("у пользователья {} нет друга {}", user.getName(), friend.getName());
        }
        log.info("пользователь {} удалил из друзей {}", user.getName(), friend.getName());
        return user;
    }

    public List<Friends> getCommonFriends(Long userId, Long otherId) {
        return friendsRepository.getCommon(userId, otherId);
    }
}
