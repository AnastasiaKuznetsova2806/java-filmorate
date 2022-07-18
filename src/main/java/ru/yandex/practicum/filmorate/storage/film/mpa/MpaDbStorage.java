package ru.yandex.practicum.filmorate.storage.film.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.referencebook.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class MpaDbStorage implements MpaStorage{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Mpa> findAllMpa() {
        String sql = "select * from MPA order by MPA_ID;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    @Override
    public Mpa findMpaById(Integer id) {
        String sql = "select * from MPA where MPA_ID = ?;";
        Mpa mpa = jdbcTemplate.query(sql, rs -> rs.next() ? makeMpa(rs) : null, id);

        if (mpa == null) {
            throw new DataNotFoundException(String.format("Рейтинг %d не найден", id));
        }
        return mpa;
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        int id = rs.getInt("MPA_ID");
        String name = rs.getString("MPA_NAME");
        return new Mpa(id, name);
    }
}
