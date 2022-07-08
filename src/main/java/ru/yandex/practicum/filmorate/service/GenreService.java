package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.referencebook.Genre;
import ru.yandex.practicum.filmorate.storage.film.genres.GenreStorage;

import javax.validation.ValidationException;
import java.util.Collection;

@Service
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    //Получение списка всех жанров
    public Collection<Genre> findAllGenres() {
        return genreStorage.findAllGenres();
    }

    //Получение жанра по уникальному идентификатору
    public Genre findGenreById(Integer id) {
        checkId(id);
        return genreStorage.findGenreById(id);
    }

    private void checkId(Integer id) {
        if (id == null) {
            throw new ValidationException("Поле id пустое!");
        }
    }
}
