package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class User {
    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;

    public boolean isValid() {
        return email != null && login != null
                && name != null && birthday != null;
    }
}
