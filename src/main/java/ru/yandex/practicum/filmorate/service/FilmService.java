package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DAL.repository.GenreRepository;
import ru.yandex.practicum.filmorate.DAL.repository.LikeRepository;
import ru.yandex.practicum.filmorate.DAL.repository.MpaRepository;
import ru.yandex.practicum.filmorate.DAL.repository.UserRepository;
import ru.yandex.practicum.filmorate.DTO.FilmDTO;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    @Autowired
    @Qualifier("filmRepository")
    private FilmStorage filmStorage;
    private final GenreRepository genreRepository;
    private final MpaRepository mpaRepository;
    private final LikeRepository likesRepository;
    private final UserRepository userRepository;

    public List<FilmDTO> getFilms() {

        HashMap<Long, List<Genre>> filmWithGenres = genreRepository.getFilmsGenres();
        return filmStorage.getFilms().stream()
                .map((film) -> film.toBuilder()
                        .genres(filmWithGenres.get(film.getId())).build())
                .toList();
    }

    public FilmDTO addFilm(FilmDTO film) {
        //проверка есть ли такой рейтинг

        if (!mpaRepository.getAllMpa().contains(film.getMpa())) {
            throw new ConditionsNotMetException("такого mpa нет");
        }
        FilmDTO newFilm = filmStorage.addFilm(film);
        List<Genre> newFilmGenres = film.getGenres();

        if (newFilmGenres == null) return newFilm;

        //проверка есть ли такие жанры
        if (!new HashSet<>(genreRepository.getAllGenres()).containsAll(newFilmGenres)) {
            throw new ConditionsNotMetException("таких жанров нет");
        }
        newFilmGenres
                .stream()
                .distinct()
                .forEach((genre) -> genreRepository.addAddGenreToFilm(newFilm.getId(), genre.getId()));
        return getFilmById(newFilm.getId());
    }

    public FilmDTO updateFilm(FilmDTO film) {
        FilmDTO oldFilm = filmStorage.getFilmById(film.getId());
        FilmDTO updatedFilm = FilmDTO.builder()
                .id(film.getId())
                .name((film.getName() == null) ? oldFilm.getName() : film.getName())
                .description((film.getDescription() == null) ? oldFilm.getDescription() : film.getDescription())
                .releaseDate((film.getReleaseDate() == null) ? oldFilm.getReleaseDate() : film.getReleaseDate())
                .duration((film.getDuration() == null) ? oldFilm.getDuration() : film.getDuration())
                .mpa((film.getMpa() == null) ? oldFilm.getMpa() : film.getMpa())
                .build();
        return filmStorage.update(updatedFilm);
    }

    public FilmDTO setLike(Long filmId, Long userId) {
        //проверяем наличие фильмы
        FilmDTO film = getFilmById(filmId);
        //проверяем есть ли пользователь с таким id
        User user = userRepository.getUserById(userId);
        likesRepository.addLike(filmId, userId);
        log.info("пользователь {} поставил лаик фильму {}", user.getName(), film.getName());
        return film;
    }

    public FilmDTO removeLike(Long filmId, Long userId) {
        FilmDTO film = getFilmById(filmId);
        //проверяем есть ли пользователь с таким id
        User user = userRepository.getUserById(userId);
        likesRepository.deleteLike(userId, filmId);
        log.info("пользователь {} удалил лаик фильму {}", user.getName(), film.getName());
        return film;

    }

    public List<Like> getPopular(int count) {
        return likesRepository.getPopular(count);
    }

    public FilmDTO getFilmById(Long id) {
        FilmDTO film = filmStorage.getFilmById(id);
        return film.toBuilder()
                .genres(genreRepository.getFilmGenres(film.getId()))
                .build();
    }


}
