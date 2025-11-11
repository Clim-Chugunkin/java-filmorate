package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;


@Value
@EqualsAndHashCode(of = {"id"})
@Builder(toBuilder = true)
public class Genre {
    long id;
    String name;
}
