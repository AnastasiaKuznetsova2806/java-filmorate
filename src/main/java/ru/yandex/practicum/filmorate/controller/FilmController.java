package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(final FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping(value = "/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на добавление объекта: '{}'", film);
        return filmService.createFilm(film);
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на обновление объекта: '{}'", film);
        return filmService.updateFilm(film);
    }

    @GetMapping(value = "/films")
    public Collection<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    @GetMapping(value = "/films/{id}")
    public Film findFilmById(@PathVariable Long id) {
        return filmService.findFilmById(id);
    }

    @PutMapping(value = "films/{id}/like/{userId}")
    public void addLike(@PathVariable Long id,
                        @PathVariable Long userId) {
        log.info("Получен запрос на добавлене лайка");
        filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "films/{id}/like/{userId}")
    public void deletingLike(@PathVariable Long id,
                             @PathVariable Long userId) {
        log.info("Получен запрос на удаление лайка");
        filmService.deletingLike(id, userId);
    }

    @GetMapping(value = "films/popular")
    public List<Film> listOfPopularMovies(@RequestParam(defaultValue = "10", required = false) int count) {
        return filmService.listOfPopularMovies(count);
    }

    @DeleteMapping(value = "films/{filmId}")
    public void deleteFilmById(@PathVariable Long filmId) {
        log.info("Получен запрос на удаление фильма '{}'", filmId);
        filmService.deleteFilmById(filmId);
    }
}
