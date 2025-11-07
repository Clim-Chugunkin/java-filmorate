package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.DTO.FilmDTO;

import java.util.List;

public interface FilmStorage {
    List<FilmDTO> getFilms();

    FilmDTO addFilm(FilmDTO film);

    FilmDTO update(FilmDTO film);

    FilmDTO getFilmById(long id);
}
