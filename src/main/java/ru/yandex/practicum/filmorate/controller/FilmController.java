package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.util.Validator;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final HashMap<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }
    //POST request
    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        Film newFilm = film.toBuilder().id(getNextId()).build();
        validationCheck(newFilm);
        films.put(newFilm.getId(), newFilm);
        log.info("добавлен новый фильм {}", newFilm.getName());
        return newFilm;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
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

        validationCheck(filmUpdated);
        films.put(filmUpdated.getId(), filmUpdated);
        log.info("фильм {} обнавлен", filmUpdated.getName());
        return filmUpdated;
    }

    private void validationCheck(Film film) throws ValidationException {
        Validator validator = new Validator();
        if (!film.isValid()) throw new ValidationException("Не все поля заполнены");
        validator.addRule(() -> film.getName().isBlank(),
                        "название не может быть пустым")
                .addRule(() -> film.getDescription().length() > 200,
                        "Описание не должно быть больше 200 символлов")
                .addRule(() -> film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)),
                        "дата релиза — не раньше 28 декабря 1895 года")
                .addRule(() -> film.getDuration() < 0,
                        "продолжительность фильма должна быть положительным числом")
                .check();
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
