package ru.yandex.practicum.filmorate.DAL;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friends;

import java.util.List;

@Repository
public class FriendsRepository extends BaseRepository<Friends> {

    private static final String ADD_NEW_FRIEND = "INSERT INTO friendship " +
            "(user_id, friend_id) " +
            "VALUES (?,?)";

    private static final String DELETE_FRIEND = "DELETE FROM friendship " +
            "WHERE user_id = ? AND friend_id = ?";

    private static final String GET_ALL_FRIENDS = "SELECT user_id, friend_id id FROM friendship " +
            "WHERE user_id = ?";

    private static final String GET_COMMON_FRIENDS = "SELECT friend_id id FROM friendship " +
            "WHERE user_id = ? " +
            "AND friend_id in (SELECT friend_id FROM friendship WHERE user_id = ?)";

    public FriendsRepository(JdbcTemplate jdbc, RowMapper<Friends> mapper) {
        super(jdbc, mapper);
    }

    public void addFriend(long userId, long friendId) {
        update(ADD_NEW_FRIEND,
                userId,
                friendId);
    }

    public int deleteFriend(long userId, long friendId) {
        return update(DELETE_FRIEND,
                userId,
                friendId);
    }

    public List<Friends> getUserFriends(long userId) {
        return findMany(GET_ALL_FRIENDS,
                userId);
    }

    public List<Friends> getCommon(long userId, long otherId) {
        return findMany(GET_COMMON_FRIENDS,
                userId,
                otherId);
    }
}
