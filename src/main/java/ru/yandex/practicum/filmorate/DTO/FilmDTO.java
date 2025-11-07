package ru.yandex.practicum.filmorate.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.annotation.MinDate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class FilmDTO {
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
    @NotNull(message = "нет рейтинга")
    Mpa mpa;
    //@NotNull(message = "не указаны жанры")
    List<Genre> genres;
}
