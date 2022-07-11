package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.friend.FriendDbStorage;
import ru.yandex.practicum.filmorate.validation.DataValidation;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage{
    private final JdbcTemplate jdbcTemplate;
    private  final FriendDbStorage friendStorage;
    private final DataValidation dataValidation;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate,
                         FriendDbStorage friendStorage,
                         DataValidation dataValidation) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendStorage = friendStorage;
        this.dataValidation = dataValidation;
    }

    @Override
    public User createUser(User user) {
        dataValidation.userdataVerification(user);

        String sql = "insert into USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) values (?, ?, ?, ?);";

        KeyHolder id = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedSt = connection.prepareStatement(sql, new String[]{"USER_ID"});
            preparedSt.setString(1, user.getEmail());
            preparedSt.setString(2, user.getLogin());
            preparedSt.setString(3, user.getName());
            preparedSt.setDate(4, Date.valueOf(user.getBirthday()));
            return preparedSt;
        }, id);
        user.setId(Objects.requireNonNull(id.getKey()).longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        dataValidation.userdataVerification(user);
        findUserById(user.getId());

        String sql = "update USERS set EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? where USER_ID = ?;";

        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public Collection<User> findAllUsers() {
        String sql = "select * from USERS;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User findUserById(Long id) {
        String sql = "select * from USERS where USER_ID = ?;";

        User user = jdbcTemplate.query(sql, rs -> rs.next() ? makeUser(rs) : null, id);

        if (user == null) {
            throw new DataNotFoundException(String.format("Пользователь %d не найден", id));
        }
        return user;
    }

    //Добавление в друзья
    public void addToFriends(Long id, Long friendId) {
        findUserById(id);
        findUserById(friendId);
        friendStorage.addToFriends(id, friendId);
    }

    //Удаление из друзей
    public void unfriending(Long id, Long friendId) {
        findUserById(id);
        findUserById(friendId);
        friendStorage.unfriending(id, friendId);
    }

    //Список пользователей, являющихся друзьями.
    public List<Long> findFriendList(Long id) {
        return friendStorage.findFriendList(id);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("USER_ID");
        String email = rs.getString("EMAIL");
        String login = rs.getString("LOGIN");
        String name = rs.getString("USER_NAME");
        LocalDate birthday = rs.getDate("BIRTHDAY").toLocalDate();
        User user = new User(email, login, name, birthday);
        user.setId(id);
        return user;
    }
}
