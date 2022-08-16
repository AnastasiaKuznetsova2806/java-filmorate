package ru.yandex.practicum.filmorate.storage.film.director;

import ru.yandex.practicum.filmorate.model.referencebook.Director;

import java.util.List;
import java.util.Set;

public interface DirectorStorage {
    //Получение списка всех режиссёров
    List<Director> findAllDirectors();

    //Получение режиссёра по уникальному идентификатору
    Director findDirectorById(long id);

    //Создание режиссёра
    Director createDirector(Director director);

    //Изменение режиссёра
    Director updateDirector(Director director);

    //Удаление режиссёра по уникальному идентификатору
    void deletingDirectorById(long id);

    //Добавление режиссёра
    void addDirector(long film_id, Set<Director> directors);

    //Поиск всех режиссёров фильма
    List<Director> findMovieDirector(long id);
}
