package ru.yandex.practicum.filmorate.DAL.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository
public class MpaRepository extends BaseRepository<Mpa> {

    private static final String FIND_MPA_BY_ID = "SELECT * FROM rating" +
            " WHERE rating_id = ?";

    private static final String FIND_ALL_MPA = "SELECT * FROM rating";
    private static final String ADD_NEW_MPA = "INSERT INTO rating (rating_id, name,description) " +
            "VALUES (?,?,?)";

    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public Mpa getMpaById(long mId) throws ConditionsNotMetException {
        return findOne(FIND_MPA_BY_ID, mId)
                .orElseThrow(() -> new ConditionsNotMetException("MPA не наден"));
    }

    public List<Mpa> getAllMpa() {
        return findMany(FIND_ALL_MPA);
    }

    public void addMpa(Mpa mpa) {
        update(ADD_NEW_MPA,
                mpa.getId(),
                mpa.getName(),
                mpa.getDescription());
    }
}
