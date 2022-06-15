package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.DataValidation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Long, Film> films = new HashMap<>();
    private final DataValidation dataValidation = new DataValidation();
    private long id = 0;

    private long increaseId() {
        return ++id;
    }

    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Film createFilm(Film film) {
        dataValidation.filmDataVerification(film);
        film.setId(increaseId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        long id = film.getId();
        if(!films.containsKey(id)) {
            throw new DataNotFoundException("Не найдена запись с id = " + id);
        }

        dataValidation.filmDataVerification(film);
        films.put(id, film);
        return film;
    }

    @Override
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @Override
    public Film findFilmById(Long id) {
        if (id == null) {
            return null;
        }
        if (!films.containsKey(id)) {
            throw new DataNotFoundException(String.format("Фильм %d не найден", id));
        }
        return films.get(id);
    }
}
