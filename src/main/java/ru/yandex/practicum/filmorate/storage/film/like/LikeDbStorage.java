package ru.yandex.practicum.filmorate.storage.film.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;

import java.util.List;

@Component
public class LikeDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Добавление лайка
    public void addLike(Long id, Long userId) {
        checkUser(userId);

        String sql = "insert into LIKES (FILM_ID, USER_ID) values (?, ?)";

        jdbcTemplate.update(sql, id, userId);
    }

    //Удаление лайка
    public void deletingLike(Long id, Long userId) {
        checkLike(id, userId);

        String sql = "delete from LIKES where FILM_ID = ? and USER_ID = ?;";

        jdbcTemplate.update(sql, id, userId);
    }

    public List<Long> findLike(long id) {
        String sql = "select USER_ID from LIKES where FILM_ID = ?";
        return jdbcTemplate.queryForList(sql, Long.class, id);
    }

    //Проверка лайка
    private void checkLike(Long id, Long userId) {
        checkUser(userId);

        String sql = "select USER_ID from LIKES where FILM_ID = ? and USER_ID = ?;";

        List<Integer> like = jdbcTemplate.queryForList(sql, Integer.class, id, userId);

        if (like.size() == 0) {
            throw new DataNotFoundException(String.format("Лайк с № %d не найден", userId));
        }
    }

    private void checkUser(Long userId) {
        String sql = "select USER_ID from USERS where USER_ID = ?;";

        List<Integer> users = jdbcTemplate.queryForList(sql, Integer.class, userId);

        if (users.size() == 0) {
            throw new DataNotFoundException(String.format("Пользователь %d не найден", userId));
        }
    }
}
