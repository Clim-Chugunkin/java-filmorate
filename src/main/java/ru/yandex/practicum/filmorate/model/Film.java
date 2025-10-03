package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.annotation.MinDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


/**
 * Film.
 */

// аннтотация @Value подставляет всем полям модификаторы private final
// и переопределяет методы equals, hashCode, toString
@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class Film implements Comparable<Film> {
    Long id;

    @NotNull(message = "не указано название")
    @NotBlank(message = "название не может быть пустым")
    String name;

    @NotNull(message = "нет описания")
    @Length(max = 200, message = "максимальная длина описания не более 200 символов")
    String description;

    @NotNull(message = "не указана дата выпуска")
    @MinDate
    LocalDate releaseDate;

    @NotNull(message = "нет продолжительности")
    @Positive(message = "продолжительность должна быть положительной")
    Integer duration;

    Set<Long> likes = new HashSet<>();

    public boolean addLike(Long userId) {
        return likes.add(userId);
    }

    public boolean removeLike(Long userId) {

        return likes.remove(userId);
    }

    @Override
    public int compareTo(Film o) {
        return o.getLikes().size() - likes.size();
    }
}
