package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

// аннтотация @Value подставляет всем полям модификаторы private final
// и переопределяет методы equals, hashCode, toString
@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class User {
    Long id;

    @NotNull(message = "не указана почта")
    @Email(message = "почта указана неправильно")
    String email;

    @NotNull(message = "не указан логин")
    @NotBlank(message = "логин не должен быть пустым")
    @Pattern(regexp = "^\\S+$", message = "логин не должен содержать пробелы")
    String login;

    String name;

    @NotNull(message = "не указана дата рождения")
    @Past(message = "дата рождения не может быть в будущем")
    LocalDate birthday;

    Set<Long> friends = new HashSet<>();

    public boolean addFriend(Long friendId) {
        return friends.add(friendId);
    }

    public boolean removeFriend(Long friendId) {
        return friends.remove(friendId);
    }

}
