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
    Film findFilmById(long id);

    //Добавить лайк
    void addLike(long id, long userId);

    //Удалить лайк
    void deletingLike(long id, long userId);

    //Удалить фильм по уникальному идентификатору
    void deleteFilmById(long filmId);

    //Получение списка фильмов режиссера отсортированных по количеству лайков или году выпуска
    List<Film> findDirectorFilms(long directorId, SortingType sorting);

    //Получить список всех любимых фильмов
    List<Film> findAllFavoriteMovies(long id);

    //Получить список рекомендованных фильмов для пользователя
    List<Film> recommendationsFilm(long id);

    //Поиск фильмов по названию
    List<Film> findFilmByTitle(String query);

    //Поиск фильмов по режиссёру
    List<Film> findFilmByDirector(String query);
}
