package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }

    public Film setLike(Long filmId, Long userId) {
        //проверяем наличие фильмы
        Film film = getFilmById(filmId);
        //проверяем есть ли пользователь с таким id
        User user = userService.getUserById(userId);
        film.addLike(userId);
        log.info("пользователь {} поставил лаик фильму {}", user.getName(), film.getName());
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        //проверяем есть ли пользователь с таким id
        User user = userService.getUserById(userId);
        film.removeLike(userId);
        log.info("пользователь {} удалил лаик фильму {}", user.getName(), film.getName());
        return film;
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getFilms()
                .stream()
                .sorted()
                .limit(count)
                .toList();
    }

    public Film getFilmById(Long id) {
        return filmStorage
                .getFilms()
                .stream()
                .filter((it) -> Objects.equals(it.getId(), id))
                .findFirst()
                .orElseThrow(() -> new ConditionsNotMetException("нет такого фильма"));
    }


}
