package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateFilmControllerTests {

    private final FilmController fm = new FilmController();

    //название не может быть пустым;
    @Test
    public void checkIfNameIsBlank() {
        Film film = new Film(0L, "", "film description", LocalDate.of(1999, 10, 10), 100);
        Exception exception = assertThrows(ValidationException.class, () -> {
            fm.addFilm(film);
        });
        String expectedMessage = "название не может быть пустым";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    //максимальная длина описания — 200 символов;
    @Test
    public void testMaxLengthDescription() {
        Film film = new Film(0L, "name", "a".repeat(200), LocalDate.of(1999, 10, 10), 100);
        assertDoesNotThrow(() -> fm.addFilm(film));
        Film notValidFilm = film.toBuilder().description("a".repeat(201)).build();
        Exception exception = assertThrows(ValidationException.class, () -> {
            fm.addFilm(notValidFilm);
        });
        String expectedMessage = "Описание не должно быть больше 200 символлов";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    //дата релиза — не раньше 28 декабря 1895 года;
    @Test
    public void releaseDayCheck() {
        Film film = new Film(0L, "name", "a".repeat(200), LocalDate.of(1895, 12, 28), 100);
        assertDoesNotThrow(() -> fm.addFilm(film));
        Film notValidFilm = film.toBuilder()
                .releaseDate(LocalDate.of(1895, 12, 27)).build();
        Exception exception = assertThrows(ValidationException.class, () -> {
            fm.addFilm(notValidFilm);
        });
        String expectedMessage = "дата релиза — не раньше 28 декабря 1895 года";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    //продолжительность фильма должна быть положительным числом
    @Test
    public void durationCheck() {
        Film film = new Film(0L, "name", "a".repeat(200), LocalDate.of(1895, 12, 28), 0);
        assertDoesNotThrow(() -> fm.addFilm(film));
        Film notValidFilm = film.toBuilder()
                .duration(-1).build();
        Exception exception = assertThrows(ValidationException.class, () -> {
            fm.addFilm(notValidFilm);
        });
        String expectedMessage = "продолжительность фильма должна быть положительным числом";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void updateFilm() {
        Film film = new Film(0L, "name", "a".repeat(200), LocalDate.of(1895, 12, 28), 0);
        assertDoesNotThrow(() -> fm.addFilm(film));
        Film tempFilm = (Film) fm.getFilms().toArray()[0];
        Long filmID = tempFilm.getId();
        Film updatedFilm = new Film(filmID, "updated", "a".repeat(200), LocalDate.of(1895, 12, 28), 12);
        assertDoesNotThrow(() -> fm.update(updatedFilm));
        Film newfilm = fm.getFilms()
                .stream()
                .filter((_film) -> Objects.equals(_film.getId(), filmID))
                .findFirst()
                .orElse(null);
        assertEquals("updated", newfilm.getName());
    }

    //попытка обновить фильм без id
    @Test
    public void updateWithoutID() {
        Film film = new Film(null, "name", "a".repeat(200), LocalDate.of(1895, 12, 28), 0);
        Exception exception = assertThrows(ConditionsNotMetException.class, () -> {
            fm.update(film);
        });
        String expectedMessage = "Id должен быть указан";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    //попытка обновить фильм id которого нет
    @Test
    public void updateWithUnexistedID() {
        Film film = new Film(1000L, "name", "a".repeat(200), LocalDate.of(1895, 12, 28), 0);
        Exception exception = assertThrows(ConditionsNotMetException.class, () -> {
            fm.update(film);
        });
        String expectedMessage = "такого фильма нет";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

}
