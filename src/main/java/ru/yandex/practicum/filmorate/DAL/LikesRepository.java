package ru.yandex.practicum.filmorate.DAL;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;

import java.time.LocalDate;
import java.util.List;

@Repository
public class LikesRepository extends BaseRepository<Like> {

    private static final String ADD_LIKE = "INSERT INTO likes (user_id,film_id, like_date) " +
            " VALUES (?,?,?)";
    private static final String DELETE_LIKE = "DELETE FROM likes " +
            "WHERE user_id = ? AND film_id = ?";

    private static final String GET_POPULAR = "SELECT film_id, COUNT(*) likes_count FROM likes " +
            "GROUP BY film_id " +
            "ORDER BY likes_count DESC " +
            "LIMIT ?";

    public LikesRepository(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);
    }

    public void addLike(long filmId, long userId) {
        update(ADD_LIKE,
                userId,
                filmId,
                LocalDate.now());
    }

    public void deleteLike(long filmId, long userId) {
        update(DELETE_LIKE,
                userId,
                filmId);
    }

    public List<Like> getPopular(int limit) {
        return findMany(GET_POPULAR,
                limit);
    }


}
