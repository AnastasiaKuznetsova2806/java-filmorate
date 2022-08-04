package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.util.sorting.SortingType;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
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
    }

    //Удаление лайка
    public void deletingLike(long id, long userId) {
        checkId(id);
        filmStorage.deletingLike(id, userId);
    }

    //Вывод наиболее популярных фильмов
    public List<Film> listOfPopularMovies(int count) {
        if (count <= 0) {
            throw new ValidationException("Поле count должно быть больше 0");
        }

        return filmStorage.findAllFilms().stream()
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

    private void checkId(Long id) {
        if (id == null) {
            throw new ValidationException("Поле id пустое!");
        }
        findFilmById(id);
    }
}
