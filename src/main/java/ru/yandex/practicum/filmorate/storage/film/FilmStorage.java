package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.sorting.SortingType;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    //Создать фильм
    Film createFilm(Film film);

    //Обновить фильм
    Film updateFilm(Film film);

    //Получить список всех фильмов
    Collection<Film> findAllFilms();

    //Получить фильм по уникальному идентификатору
    Film findFilmById(Long id);

    //Добавить лайк
    void addLike(Long id, Long userId);

    //Удалить лайк
    void deletingLike(Long id, Long userId);

    //Удалить фильм
    void deleteFilmById(Long filmId);

    //Получение списка фильмов режиссера отсортированных по количеству лайков или году выпуска
    List<Film> findDirectorFilms(long directorId, SortingType sorting);

    //Получить список всех любимых фильмов
    List<Film> findAllFavoriteMovies(Long id);

    //Получить список рекомендованных фильмов для пользователя
    List<Film> recommendationsFilm(Long id);
}
