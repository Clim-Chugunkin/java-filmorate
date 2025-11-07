package ru.yandex.practicum.filmorate.DAL;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.DTO.FilmDTO;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@ComponentScan("ru.yandex.practicum.filmorate")
@RequiredArgsConstructor
class FilmRepositoryTest {

    @Autowired
    @Qualifier("filmRepository")
    private FilmStorage filmStorage;

    @Autowired
    private MpaRepository mpaRepository;

    @BeforeEach
    public void setUP() {
        mpaRepository.addMpa(Mpa.builder()
                .id(100)
                .name("G")
                .description("desc")
                .build());
    }

    @Test
    public void addAndGetFilm() {
        FilmDTO film = FilmDTO.builder()
                .name("new film")
                .description("new film description")
                .releaseDate(LocalDate.of(2001, 10, 10))
                .mpa(Mpa.builder().id(100).build())
                .duration(100)
                .build();
        film = filmStorage.addFilm(film);
        assertEquals("new film", filmStorage.getFilmById(film.getId()).getName());
    }

    @Test
    public void addAndGetListOfFilms() {
        FilmDTO film = FilmDTO.builder()
                .name("new film")
                .description("new film description")
                .releaseDate(LocalDate.of(2001, 10, 10))
                .mpa(Mpa.builder().id(100).build())
                .duration(100)
                .build();

        FilmDTO film1 = FilmDTO.builder()
                .name("new film2")
                .description("new film2 description")
                .releaseDate(LocalDate.of(2001, 10, 10))
                .mpa(Mpa.builder().id(100).build())
                .duration(100)
                .build();
        filmStorage.addFilm(film);
        filmStorage.addFilm(film1);
        assertEquals(2, filmStorage.getFilms().size());
    }

    @Test
    public void updateFilmTest() {
        FilmDTO film = FilmDTO.builder()
                .name("new film")
                .description("new film description")
                .releaseDate(LocalDate.of(2001, 10, 10))
                .mpa(Mpa.builder().id(100).build())
                .duration(100)
                .build();
        film = filmStorage.addFilm(film);
        FilmDTO updated = film.toBuilder()
                .name("updated film name")
                .build();
        filmStorage.update(updated);
        assertEquals("updated film name", filmStorage.getFilmById(film.getId()).getName());
    }

}