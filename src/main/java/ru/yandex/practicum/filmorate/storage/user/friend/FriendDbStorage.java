package ru.yandex.practicum.filmorate.storage.user.friend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.referencebook.FriendStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FriendDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Добавление в друзья
    public void addToFriends(long id, long friendId) {
        String sql = "insert into FRIENDS (USER_ID, FRIEND_ID) values (?, ?);";

        jdbcTemplate.update(sql, id, friendId);

        checkStatus(id, friendId);
        checkStatus(friendId, id);
    }

    //Удаление из друзей
    public void unfriending(long id, long friendId) {
        String sql = "delete from FRIENDS where USER_ID = ? and FRIEND_ID = ?";

        jdbcTemplate.update(sql, id, friendId);

        checkStatus(friendId, id);
    }

    //Список пользователей, являющихся друзьями.
    public List<Long> findFriendList(long id) {
        String sql = "select FRIEND_ID from FRIENDS where USER_ID = ?;";

        return jdbcTemplate.queryForList(sql, Long.class, id);
    }

    private FriendStatus makeFriendStatus(ResultSet rs) throws SQLException {
        int status_id = rs.getInt("STATUS");
        return new FriendStatus(status_id);
    }

    //Обновление статуса дружбы
    private void updateFriendStatus(long id, long friendId, int status) {
        String sql = "update FRIENDS set STATUS_ID = ? where USER_ID = ? and FRIEND_ID = ?;";
        jdbcTemplate.update(sql, status, id, friendId);
    }

    //Проверка статуса дружбы
    private void checkStatus (long id, long friendId) {
        String sql = "select " +
                "  case " +
                "    when f2.USER_ID is null then 1 " +
                "    else 2 " +
                "  end status " +
                "from FRIENDS f " +
                "         left join FRIENDS f2 ON f2.USER_ID = f.FRIEND_ID " +
                "where f.USER_ID = ? and f.FRIEND_ID = ?; ";

        FriendStatus status = jdbcTemplate.query(sql, rs -> rs.next() ? makeFriendStatus(rs) : null, id, friendId);

        if (status != null) {
            updateFriendStatus(id, friendId, status.getStatusId());
        }
    }
}
