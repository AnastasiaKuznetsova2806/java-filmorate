package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.DataValidation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();
    private final DataValidation dataValidation = new DataValidation();

    private int increaseId() {
        return ++id;
    }

    @PostMapping(value = "/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на добавление объекта: '{}'", film);

        dataValidation.filmDataVerification(film);
        film.setId(increaseId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на обновление объекта: '{}'", film);

        int id = film.getId();
        if(!films.containsKey(id)) {
            throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "Не найдена запись с id = " + id);
        }

        dataValidation.filmDataVerification(film);
        films.put(id, film);
        return film;
    }

    @GetMapping(value = "/films")
    public Collection<Film> findAllFilms() {
        return films.values();
    }
}
