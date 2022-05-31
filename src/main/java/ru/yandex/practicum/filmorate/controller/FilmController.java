package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private static final LocalDate BIRTHDAY_MOVIE = LocalDate.of(1895, 12, 28);
    private int id = 0;

    private final Map<Integer, Film> films = new HashMap<>();

    private int increaseId() {
        return ++id;
    }

    @PostMapping(value = "/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на добавление объекта: '{}'", film);

        dataCheck(film);
        film.setId(increaseId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на обновление объекта: '{}'", film);

        int id = film.getId();
        if(!films.containsKey(id)) {
            throw new ValidationException("Не найдена запись с id = " + id);
        }

        dataCheck(film);
        films.put(id, film);
        return film;
    }

    @GetMapping(value = "/films")
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    private void dataCheck(Film film) throws ValidationException{
        LocalDate date = film.getReleaseDate();
        if (date.isBefore(BIRTHDAY_MOVIE)) {
            throw new ValidationException("Дата релиза раньше 28 декабря 1895 года");
        }
    }
}
