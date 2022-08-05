package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.referencebook.Director;
import ru.yandex.practicum.filmorate.model.referencebook.Genre;
import ru.yandex.practicum.filmorate.model.referencebook.Mpa;
import ru.yandex.practicum.filmorate.storage.film.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.genres.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.util.sorting.SortingType;
import ru.yandex.practicum.filmorate.validation.DataValidation;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final LikeDbStorage likeStorage;
    private final GenreStorage genreStorage;
    private final DirectorStorage directorStorage;
    private final DataValidation dataValidation;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         LikeDbStorage likeStorage,
                         GenreStorage genreStorage,
                         DirectorStorage directorStorage,
                         DataValidation dataValidation) {
        this.jdbcTemplate = jdbcTemplate;
        this.likeStorage = likeStorage;
        this.genreStorage = genreStorage;
        this.directorStorage = directorStorage;
        this.dataValidation = dataValidation;
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
        directorStorage.addDirector(film.getId(), film.getDirectors());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        dataValidation.filmDataVerification(film);
        findFilmById(film.getId());

        String sql = "update FILMS " +
                "set FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? " +
                "where FILM_ID = ? ";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        genreStorage.addGenre(film.getId(), film.getGenres());

        if (film.getDirectors().isEmpty()) {
            film.setDirectors(null);
        }
        directorStorage.addDirector(film.getId(), film.getDirectors());
        return film;
    }

    @Override
    public Collection<Film> findAllFilms() {
        String sql = "select * " +
                "from FILMS F " +
                "left join MPA M on F.MPA_ID = M.MPA_ID ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film findFilmById(long id) {
        String sql = "select * " +
                "from FILMS F " +
                "left join MPA M on F.MPA_ID = M.MPA_ID " +
                "where F.FILM_ID = ?; ";

        Film film = jdbcTemplate.query(sql, rs -> rs.next() ? makeFilm(rs) : null, id);

        if (film == null) {
            throw new DataNotFoundException(String.format("Фильм %d не найден", id));
        }
        return film;
    }

    //Добавление лайка
    @Override
    public void addLike(long id, long userId) {
        likeStorage.addLike(id, userId);
    }

    //Удаление лайка
    @Override
    public void deletingLike(long id, long userId) {
        likeStorage.deletingLike(id, userId);
    }

    //Удаление фильма
    @Override
    public void deleteFilmById(long filmId) {
        findFilmById(filmId);

        String sql = "delete from FILMS where FILM_ID = ?";

        jdbcTemplate.update(sql, filmId);
    }

    //Получение списка фильмов режиссера отсортированных по количеству лайков или году выпуска
    @Override
    public List<Film> findDirectorFilms(long directorId, SortingType sorting) {
        directorStorage.findDirectorById(directorId);

        String sql = "select * " +
                "from FILM_DIRECTORS FD " +
                "join DIRECTORS D on D.DIRECTOR_ID = FD.DIRECTOR_ID " +
                "join FILMS F on FD.FILM_ID = F.FILM_ID " +
                "join MPA M on F.MPA_ID = M.MPA_ID " +
                "where FD.DIRECTOR_ID = ?; ";

        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), directorId);

        if (SortingType.LIKES.equals(sorting)) {
            return films.stream()
                    .sorted()
                    .collect(Collectors.toList());
        }
        return films.stream()
                .sorted(this::compare)
                .collect(Collectors.toList());
    }

    //Получение списка всех любимых фильмов
    @Override
    public List<Film> findAllFavoriteMovies(long id) {
        String sql = "select * " +
                "from LIKES L " +
                "join FILMS F on F.FILM_ID = L.FILM_ID " +
                "left join MPA M on F.MPA_ID = M.MPA_ID " +
                "where L.USER_ID = ?; ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id);
    }

    //Получение списка рекомендованных фильмов для пользователя
    @Override
    public List<Film> recommendationsFilm(long id) {
        String sql = "with GENERAL_FILMS as ( " +
                "select" +
                "    l.USER_ID user_recommendations, " +
                "    count(1) cnt_films " +
                "from LIKES l " +
                "join LIKES l2 on l2.FILM_ID = l.FILM_ID " +
                "                     and l2.USER_ID != l.USER_ID " +
                "where l2.USER_ID = ? " +
                "group by user_recommendations " +
                "order by cnt_films desc " +
                ") " +
                "select * " +
                "from LIKES L " +
                "join FILMS F on F.FILM_ID = L.FILM_ID " +
                "left join MPA M on F.MPA_ID = M.MPA_ID " +
                "where L.USER_ID = (select user_recommendations " +
                "                   from GENERAL_FILMS " +
                "                   group by user_recommendations " +
                "                   limit 1); ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id);
    }

    @Override
    public List<Film> findFilmByTitle(String query) {
        String sql = "select * " +
                "from FILMS F " +
                "left join MPA M on F.MPA_ID = M.MPA_ID " +
                "where lcase(F.FILM_NAME) like '%" + query.toLowerCase() + "%'; ";

        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));

        return films.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> findFilmByDirector(String query) {
        String sql = "select * " +
                "from DIRECTORS D " +
                "left join FILM_DIRECTORS FD on D.DIRECTOR_ID = FD.DIRECTOR_ID " +
                "left join FILMS F on F.FILM_ID = FD.FILM_ID " +
                "left join MPA M on F.MPA_ID = M.MPA_ID " +
                "where upper(D.DIRECTOR_NAME) like '%" + query.toUpperCase() + "%'; ";

        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));

        return films.stream()
                .sorted()
                .collect(Collectors.toList());
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

        List<Director> directors = directorStorage.findMovieDirector(film.getId());
        if (directors != null) {
            film.getDirectors().addAll(directors);
        }
        return film;
    }

    private int compare(Film f0, Film f1) {
        return (f0.getReleaseDate().compareTo(f1.getReleaseDate()));
    }
}
