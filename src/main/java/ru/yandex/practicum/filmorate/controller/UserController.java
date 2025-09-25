package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.util.Validator;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        User newUser = user.toBuilder()
                .id(getNextId())
                //имя для отображения может быть пустым — в таком случае будет использован логин
                .name(((user.getName() == null || user.getName().isBlank())) ? user.getLogin() : user.getName())
                .build();
        users.put(newUser.getId(), newUser);
        log.info("Добавлен пользователь {}", newUser.getName());
        return newUser;
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
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
        log.info("пользователь {} обнавлен", userUpdated.getLogin());
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
