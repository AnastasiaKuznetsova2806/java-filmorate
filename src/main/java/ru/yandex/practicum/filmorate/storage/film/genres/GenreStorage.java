package ru.yandex.practicum.filmorate.storage.film.genres;

import ru.yandex.practicum.filmorate.model.referencebook.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface GenreStorage {
    //Добавление жанра
    void addGenre(long film_id, Set<Genre> genres);

    //Поиск всех жанров фильма
    List<Genre> findMovieGenre(long id);

    //Получение списка всех жанров
    Collection<Genre> findAllGenres();

    //Получение жанра по уникальному идентификатору
    Genre findGenreById(int id);
}
