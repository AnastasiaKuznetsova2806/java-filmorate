package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.referencebook.Director;
import ru.yandex.practicum.filmorate.storage.film.director.DirectorStorage;

import java.util.List;

@Service
public class DirectorService {
    private final DirectorStorage directorStorage;

    @Autowired
    public DirectorService(final DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    //Получение списка всех режиссёров
    public List<Director> findAllDirectors() {
        return directorStorage.findAllDirectors();
    }

    //Получение режиссёра по уникальному идентификатору
    public Director findDirectorById(long id) {
        return directorStorage.findDirectorById(id);
    }

    //Создание режиссёра
    public Director createDirector(Director director) {
        return directorStorage.createDirector(director);
    }

    //Изменение режиссёра
    public Director updateDirector(Director director) {
        return directorStorage.updateDirector(director);
    }

    //Удаление режиссёра по уникальному идентификатору
    public void deletingDirectorById(long id) {
        directorStorage.deletingDirectorById(id);
    }
}
