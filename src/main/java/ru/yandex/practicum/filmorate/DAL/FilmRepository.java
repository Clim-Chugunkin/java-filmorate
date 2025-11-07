package ru.yandex.practicum.filmorate.DAL;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.DTO.FilmDTO;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Repository
public class FilmRepository extends BaseRepository<FilmDTO> implements FilmStorage {

    private static final String FIND_ALL_FILMS = "SELECT f.*, r.name mpa FROM films f " +
            "LEFT JOIN rating r ON f.rating_id = r.rating_id";

    private static final String FIND_FILM_BY_ID = FIND_ALL_FILMS +
            " WHERE film_id = ?";
    private static final String ADD_NEW_FILM = "INSERT INTO films (film_id,name, description," +
            "release, duration,rating_id) VALUES(?,?,?,?,?,?)";
    private static final String FILM_UPDATE = "UPDATE films SET" +
            " name = ?, description = ?, release = ?, duration = ?, rating_id = ? " +
            " WHERE film_id = ?";


    public FilmRepository(JdbcTemplate jdbc, RowMapper<FilmDTO> mapper) {
        super(jdbc, mapper);
    }


    @Override
    public List<FilmDTO> getFilms() {
        return findMany(FIND_ALL_FILMS);
    }

    @Override
    public FilmDTO addFilm(FilmDTO film) {
        Long id = getLastId("films", "film_id")
                .orElse(0L);
        update(ADD_NEW_FILM,
                ++id,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        return getFilmById(id);
    }

    @Override
    public FilmDTO update(FilmDTO film) {
        int row = update(FILM_UPDATE,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (row == 0) {
            throw new ConditionsNotMetException("такогог фильма нет");
        }
        return getFilmById(film.getId());
    }

    @Override
    public FilmDTO getFilmById(long id) {
        return findOne(FIND_FILM_BY_ID, id)
                .orElseThrow(() -> new ConditionsNotMetException("такогог фильма нет"));
    }
}

