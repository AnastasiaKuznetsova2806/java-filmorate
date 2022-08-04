package ru.yandex.practicum.filmorate.storage.film.genres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.referencebook.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
public class GenreDbStorage implements GenreStorage{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addGenre(long film_id, Set<Genre> genres) {
        checkingForDuplicateGenres(film_id);

        String sql = "insert into FILM_GENRES(film_id, genre_id) VALUES (?, ?); ";

        if (genres.size() > 0) {
            for (Genre genre : genres) {
                int idGenre = genre.getId();
                findGenreById(idGenre);

                jdbcTemplate.update(sql, film_id, idGenre);
            }
        }
    }

    public List<Genre> findMovieGenre(long id) {
        String sql = "select G.GENRE_ID, " +
                "       G.GENRE_NAME " +
                "from FILM_GENRES FG " +
                "join GENRES G on G.GENRE_ID = FG.GENRE_ID " +
                "where FG.FILM_ID = ? " +
                "order by G.GENRE_ID; ";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id);
    }

    @Override
    public Collection<Genre> findAllGenres() {
        String sql = "select * from GENRES order by GENRE_ID; ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Genre findGenreById(Integer id) {
        String sql = "select * from GENRES where GENRE_ID = ? order by GENRE_ID; ";
        Genre genre = jdbcTemplate.query(sql, rs -> rs.next() ? makeGenre(rs) : null, id);

        if (genre == null) {
            throw new DataNotFoundException(String.format("Жанр %d не найден", id));
        }
        return genre;
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("GENRE_ID");
        String name = rs.getString("GENRE_NAME");
        return new Genre(id, name);
    }

    private void checkingForDuplicateGenres(long film_id) {
        List<Genre> genre = findMovieGenre(film_id);
        if (genre.size() > 0) {
            String sql = "delete from FILM_GENRES where FILM_ID = ?";
            jdbcTemplate.update(sql, film_id);
        }
    }
}
