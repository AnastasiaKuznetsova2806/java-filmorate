package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.referencebook.Genre;
import ru.yandex.practicum.filmorate.model.referencebook.Mpa;
import ru.yandex.practicum.filmorate.storage.film.genres.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.film.like.LikeDbStorage;
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
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final DataValidation dataValidation = new DataValidation();
    private final JdbcTemplate jdbcTemplate;
    private final LikeDbStorage likeStorage;
    private final GenreDbStorage genreStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, LikeDbStorage likeStorage, GenreDbStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.likeStorage = likeStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public Film createFilm(Film film) {
        dataValidation.filmDataVerification(film);

        String sql = "insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                "values ( ?, ?, ?, ?, ? )";

        KeyHolder id = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedSt = connection.prepareStatement(sql, new String[]{"FILM_ID"});
            preparedSt.setString(1, film.getName());
            preparedSt.setString(2, film.getDescription());
            preparedSt.setDate(3, Date.valueOf(film.getReleaseDate()));
            preparedSt.setInt(4, film.getDuration());
            preparedSt.setLong(5, film.getMpa().getId());
            return preparedSt;
        }, id);
        film.setId(Objects.requireNonNull(id.getKey()).longValue());

        genreStorage.addGenre(film.getId(), film.getGenres());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        dataValidation.filmDataVerification(film);
        findFilmById(film.getId());

        String sql = "update FILMS " +
                "set FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ?" +
                "where FILM_ID = ?";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        genreStorage.addGenre(film.getId(), film.getGenres());
        return film;
    }

    @Override
    public Collection<Film> findAllFilms() {
        String sql = "select *\n" +
                "from FILMS F\n" +
                "left join MPA M on F.MPA_ID = M.MPA_ID;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film findFilmById(Long id) {
        String sql = "select *\n" +
                "from FILMS F\n" +
                "left join MPA M on F.MPA_ID = M.MPA_ID\n" +
                "where F.FILM_ID = ?;";

        Film film = jdbcTemplate.query(sql, rs -> rs.next() ? makeFilm(rs) : null, id);

        if (film == null) {
            throw new DataNotFoundException(String.format("Фильм %d не найден", id));
        }
        return film;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        long id = rs.getLong("FILM_ID");
        String name = rs.getString("FILM_NAME");
        String description = rs.getString("DESCRIPTION");
        LocalDate releaseDate = rs.getDate("RELEASE_DATE").toLocalDate();
        int duration = rs.getInt("DURATION");
        int mpaId = rs.getInt("MPA_ID");
        String mpaName = rs.getString("MPA_NAME");

        Mpa mpa = new Mpa(mpaId, mpaName);

        Film film = new Film(name, description, releaseDate, duration, mpa);
        film.setId(id);

        List<Genre> genres = genreStorage.findMovieGenre(film.getId());
        if (genres != null) {
            film.getGenres().addAll(genres);
        }

        List<Long> likes = likeStorage.findLike(film.getId());
        if (likes != null) {
            film.getLikes().addAll(likes);
        }
        return film;
    }

    //Добавление лайка
    @Override
    public void addLike(Long id, Long userId) {
        likeStorage.addLike(id, userId);
    }

    //Удаление лайка
    @Override
    public void deletingLike(Long id, Long userId) {
        likeStorage.deletingLike(id, userId);
    }
}
