package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.referencebook.Feed;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.feed.FeedDbStorage;
import ru.yandex.practicum.filmorate.util.sorting.SortingType;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final FeedDbStorage feedStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       FeedDbStorage feedStorage,
                       UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.feedStorage = feedStorage;
        this.userStorage = userStorage;
    }

    //Создание фильма
    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    //Обновление фильма
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    //Получение списка всех фильмов
    public Collection<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    //Получение фильма по уникальному идентификатору
    public Film findFilmById(long id) {
        return filmStorage.findFilmById(id);
    }

    //Добавление лайка
    public void addLike(long id, long userId) {
        checkId(id);
        filmStorage.addLike(id, userId);

        Feed feed = new Feed(userId, "LIKE", "ADD", id);
        feedStorage.createFeed(feed);
    }

    //Удаление лайка
    public void deletingLike(long id, long userId) {
        checkId(id);
        filmStorage.deletingLike(id, userId);

        Feed feed = new Feed(userId, "LIKE", "REMOVE", id);
        feedStorage.createFeed(feed);
    }

    //Вывод наиболее популярных фильмов
    public List<Film> listOfPopularMovies(int count, Integer genreId, Integer year) {
        if (count <= 0) {
            throw new ValidationException("Поле count должно быть больше 0");
        }

        Collection<Film> films = findAllFilms();

        if (genreId != null) {
            films = films.stream()
                    .filter(film -> film.getGenres().stream().anyMatch(g -> genreId.equals(g.getId())))
                    .collect(Collectors.toList());
        }

        if (year != null) {
            films = films.stream()
                    .filter(film -> year.equals(film.getReleaseDate().getYear()))
                    .collect(Collectors.toList());
        }

        return films.stream()
                .sorted()
                .limit(count)
                .collect(Collectors.toList());
    }

    //Удаление фильма
    public void deleteFilmById(long filmId) {
        filmStorage.deleteFilmById(filmId);
    }

    //Получение списка фильмов режиссера отсортированных по количеству лайков или году выпуска
    public List<Film> findDirectorFilms(long directorId, SortingType sorting) {
        return filmStorage.findDirectorFilms(directorId, sorting);
    }

    //Поиск по названию фильмов и по режиссёру
    public List<Film> searchFilm(String query, String titleAndDirector) {
        List<Film> films = new ArrayList<>();

        String[] strRequest = titleAndDirector.split(",");
        switch (strRequest.length) {
            case (1):
                if (strRequest[0].equals("title")) {
                    return filmStorage.findFilmByTitle(query);
                } else {
                    return filmStorage.findFilmByDirector(query);
                }
            case (2):
                films = filmStorage.findFilmByTitle(query);
                films.addAll(filmStorage.findFilmByDirector(query));
        }
        return films.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    //Получение списка общих с другом фильмов
    public List<Film> findListOfCommonFilms(long userId, long friendId) {
        userStorage.findUserById(userId);
        userStorage.findUserById(friendId);

        List<Film> filmsUser = filmStorage.findAllFavoriteMovies(userId);
        List<Film> filmsFriend = filmStorage.findAllFavoriteMovies(friendId);

        return filmsUser.stream()
                .filter(filmsFriend::contains)
                .sorted()
                .collect(Collectors.toList());
    }

    private void checkId(Long id) {
        if (id == null) {
            throw new ValidationException("Поле id пустое!");
        }
        findFilmById(id);
    }
}
