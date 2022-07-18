package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.referencebook.Mpa;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaStorage;

import java.util.Collection;

@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    //Получение списка всех рейтингов
    public Collection<Mpa> findAllMpa() {
        return mpaStorage.findAllMpa();
    }

    //Получение рейтинга по уникальному идентификатору
    public Mpa findMpaById(Integer id) {
        return mpaStorage.findMpaById(id);
    }
}
