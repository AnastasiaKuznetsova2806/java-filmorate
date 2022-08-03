package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    //Создать фильм
    Film createFilm(Film film);

    //Обновить фильм
    Film updateFilm(Film film);

    //Получить список всех фильмов
    Collection<Film> findAllFilms();

    //Получить фильм по уникальному идентификатору
    Film findFilmById(Long id);

    //Добавление лайка
    void addLike(Long id, Long userId);

    //Удаление лайка
    void deletingLike(Long id, Long userId);

    //Удаление фильма
    void deleteFilmById(Long filmId);
}
