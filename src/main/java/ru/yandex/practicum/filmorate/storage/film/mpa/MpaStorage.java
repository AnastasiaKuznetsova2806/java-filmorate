package ru.yandex.practicum.filmorate.storage.film.mpa;

import ru.yandex.practicum.filmorate.model.referencebook.Mpa;

import java.util.Collection;

public interface MpaStorage {
    //Получение списка всех рейтингов
    Collection<Mpa> findAllMpa();

    //Получение рейтинга по уникальному идентификатору
    Mpa findMpaById(Integer id);
}
