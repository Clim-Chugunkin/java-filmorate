package ru.yandex.practicum.filmorate.DAL.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Repository
public class UserRepository extends BaseRepository<User> implements UserStorage {

    private static final String GET_ALL_USERS = "SELECT * FROM users";
    private static final String GET_USER_BY_ID = GET_ALL_USERS + " WHERE user_id = ?";
    private static final String ADD_NEW_USER = "INSERT INTO users (user_id, email, login," +
            "name,birthday) VALUES(?,?,?,?,?)";

    private static final String USER_UPDATE = "UPDATE users SET " +
            "email = ?, login = ?, name = ?, birthday = ? " +
            " where user_id = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }


    @Override
    public List<User> getUsers() {
        return findMany(GET_ALL_USERS);
    }

    @Override
    public User addUser(User user) {
        Long id = getLastId("users", "user_id")
                .orElse(0L);
        update(ADD_NEW_USER,
                ++id,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        return getUserById(id);
    }

    @Override
    public User update(User user) {
        int row = update(USER_UPDATE,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        if (row == 0) {
            throw new ConditionsNotMetException("такогог пользователя нет");
        }
        return getUserById(user.getId());
    }

    public User getUserById(long id) {
        return findOne(GET_USER_BY_ID, id)
                .orElseThrow(() -> new ConditionsNotMetException("такого пользователя нет"));
    }
}
