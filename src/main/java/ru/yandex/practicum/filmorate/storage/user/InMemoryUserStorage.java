package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        User newUser = user.toBuilder()
                .id(getNextId())
                //имя для отображения может быть пустым — в таком случае будет использован логин
                .name(((user.getName() == null || user.getName().isBlank())) ? user.getLogin() : user.getName())
                .build();
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        User oldUser = users.get(user.getId());
        if (oldUser == null) {
            throw new ConditionsNotMetException("такого пользовтеля нет");
        }
        User userUpdated = oldUser.toBuilder()
                .name((user.getName() != null) ? user.getName() : oldUser.getName())
                .email((user.getEmail() != null) ? user.getEmail() : oldUser.getEmail())
                .login((user.getLogin() != null) ? user.getLogin() : oldUser.getLogin())
                .birthday((user.getBirthday() != null) ? user.getBirthday() : oldUser.getBirthday())
                .build();

        users.put(userUpdated.getId(), userUpdated);
        return userUpdated;
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
