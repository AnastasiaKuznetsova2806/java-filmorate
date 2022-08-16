package ru.yandex.practicum.filmorate.storage.film.director;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.referencebook.Director;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class DirectorDbStorage implements DirectorStorage{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Director> findAllDirectors() {
        String sql = "select * from DIRECTORS order by DIRECTOR_ID; ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeDirector(rs));
    }

    @Override
    public Director findDirectorById(long id) {
        String sql = "select * from DIRECTORS where DIRECTOR_ID = ? order by DIRECTOR_ID; ";

        Director director = jdbcTemplate.query(sql, rs -> rs.next() ? makeDirector(rs):null, id);

        if (director == null) {
            throw new DataNotFoundException(String.format("Режиссёр %d не найден", id));
        }
        return director;
    }

    @Override
    public Director createDirector(Director director) {
        String sql = "insert into DIRECTORS(DIRECTOR_NAME) values (?); ";

        KeyHolder id = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedSt = connection.prepareStatement(sql, new String[]{"DIRECTOR_ID"});
            preparedSt.setString(1, director.getName());
            return preparedSt;
        }, id);
        director.setId(Objects.requireNonNull(id.getKey()).longValue());
        return director;
    }

    @Override
    public Director updateDirector(Director director) {
        findDirectorById(director.getId());

        String sql = "update DIRECTORS set DIRECTOR_ID = ?, DIRECTOR_NAME = ?; ";

        jdbcTemplate.update(sql,
                director.getId(),
                director.getName());
        return director;
    }

    @Override
    public void deletingDirectorById(long id) {
        findDirectorById(id);

        String sql = "delete from DIRECTORS where DIRECTOR_ID = ?; ";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void addDirector(long film_id, Set<Director> directors) {
        checkingForDuplicateDirector(film_id);

        String sql = "insert into FILM_DIRECTORS(FILM_ID, DIRECTOR_ID) values (?, ?); ";

        if (directors != null && directors.size() > 0) {
            for (Director director : directors) {
                long idDirector = director.getId();
                findDirectorById(idDirector);

                jdbcTemplate.update(sql, film_id, idDirector);
            }
        }
    }

    @Override
    public List<Director> findMovieDirector(long id) {
        String sql = "select D.DIRECTOR_ID, " +
                "       D.DIRECTOR_NAME " +
                "from FILM_DIRECTORS FD " +
                "join DIRECTORS D on FD.DIRECTOR_ID = D.DIRECTOR_ID " +
                "where FD.FILM_ID = ? " +
                "order by D.DIRECTOR_ID; ";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeDirector(rs), id);
    }

    private Director makeDirector(ResultSet rs) throws SQLException {
        long id = rs.getInt("DIRECTOR_ID");
        String name = rs.getString("DIRECTOR_NAME");
        return new Director(id, name);
    }

    private void checkingForDuplicateDirector(long film_id) {
        List<Director> directors = findMovieDirector(film_id);
        if (directors.size() > 0) {
            String sql = "delete from FILM_DIRECTORS where FILM_ID = ?";
            jdbcTemplate.update(sql, film_id);
        }
    }
}
