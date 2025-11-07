package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.DTO.FilmDTO;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;


    @GetMapping
    public List<FilmDTO> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public FilmDTO getFilmById(@PathVariable long id) {
        return filmService.getFilmById(id);
    }

    @PostMapping
    public FilmDTO addFilm(@Valid @RequestBody FilmDTO film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public FilmDTO update(@RequestBody FilmDTO film) {
        return filmService.updateFilm(film);
    }

    //PUT /films/{id}/like/{userId}  — пользователь ставит лайк фильму.
    @PutMapping("/{filmId}/like/{userId}")
    public FilmDTO setLike(@PathVariable Long filmId,
                           @PathVariable Long userId) {
        return filmService.setLike(filmId, userId);
    }

    //DELETE /films/{id}/like/{userId}  — пользователь удаляет лайк
    @DeleteMapping("/{filmId}/like/{userId}")
    public FilmDTO removeLike(@PathVariable Long filmId,
                              @PathVariable Long userId) {
        return filmService.removeLike(filmId, userId);
    }

    //GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, верните первые 10.
    @GetMapping("/popular")
    public List<Like> getPopular(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopular(count);
    }
}
