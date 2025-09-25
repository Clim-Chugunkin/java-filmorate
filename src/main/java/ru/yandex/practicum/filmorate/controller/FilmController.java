package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.util.Validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final HashMap<Long, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film)  {
        Film newFilm = film.toBuilder().id(getNextId()).build();
        films.put(newFilm.getId(), newFilm);
        log.info("добавлен новый фильм {}", newFilm.getName());
        return newFilm;
    }

    @PutMapping
    public Film update(@RequestBody Film film)  {
        if (film.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        Film oldFilm = films.get(film.getId());
        if (oldFilm == null) {
            throw new ConditionsNotMetException("такого фильма нет");
        }
        Film filmUpdated = oldFilm.toBuilder()
                .name((film.getName() != null) ? film.getName() : oldFilm.getName())
                .description((film.getDescription() != null) ? film.getDescription() : oldFilm.getDescription())
                .releaseDate((film.getReleaseDate() != null) ? film.getReleaseDate() : oldFilm.getReleaseDate())
                .duration((film.getDuration() != null) ? film.getDuration() : oldFilm.getDuration())
                .build();

        films.put(filmUpdated.getId(), filmUpdated);
        log.info("фильм {} обнавлен", filmUpdated.getName());
        return filmUpdated;
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
