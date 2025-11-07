package ru.yandex.practicum.filmorate.DAL;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
public class GenreRepository extends BaseRepository<Genre> {

    private static final String FIND_FILM_GENRE = "SELECT  g.*  FROM films_genres fg" +
            " INNER JOIN genre g ON g.genre_id = fg.genre_id" +
            " WHERE fg.film_id = ?";

    private static final String FIND_ALL_GENRE = "SELECT * FROM genre";
    private static final String GET_GENRE_BY_ID = "SELECT * FROM genre WHERE genre_id = ?";
    private static final String ADD_GENRE_TO_FILM = "INSERT INTO films_genres" +
            "(film_id,genre_id) VALUES (?,?)";


    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> getFilmGenres(long filmId) {
        return findMany(FIND_FILM_GENRE, filmId);
    }

    public void addAddGenreToFilm(long filmId, long genreId) {
        update(ADD_GENRE_TO_FILM,
                filmId, genreId);

    }

    public List<Genre> getAllGenres() {
        return findMany(FIND_ALL_GENRE);
    }

    public Genre getGenreById(long id) {
        return findOne(GET_GENRE_BY_ID, id)
                .orElseThrow(() -> new ConditionsNotMetException("такого жанра нет"));
    }
}
