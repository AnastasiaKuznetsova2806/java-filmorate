package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.util.sorting.SortingType;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
public class FilmController {
    private static final String COUNT_FILMS = "10";
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
    public Film findFilmById(@PathVariable long id) {
        return filmService.findFilmById(id);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public void addLike(@PathVariable long id,
                        @PathVariable long userId) {
        log.info("Получен запрос на добавлене лайка - '{}' пользувателю - '{}'", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void deletingLike(@PathVariable long id,
                             @PathVariable long userId) {
        log.info("Получен запрос на удаление лайка - '{}' пользувателю - '{}'", id, userId);
        filmService.deletingLike(id, userId);
    }

    @GetMapping(value = "/films/popular")
    public List<Film> listOfPopularMovies(@RequestParam(defaultValue = COUNT_FILMS, required = false) int count,
                                          @RequestParam(required = false) Integer genreId,
                                          @RequestParam(required = false) Integer year) {
        return filmService.listOfPopularMovies(count, genreId, year);
    }

    @DeleteMapping(value = "/films/{filmId}")
    public void deleteFilmById(@PathVariable long filmId) {
        log.info("Получен запрос на удаление фильма '{}'", filmId);
        filmService.deleteFilmById(filmId);
    }

    @GetMapping(value = "/films/director/{directorId}")
    public List<Film> findDirectorFilms(@PathVariable long directorId,
                                        @RequestParam("sortBy") SortingType sorting) {
        return filmService.findDirectorFilms(directorId, sorting);
    }

    @GetMapping(value = "/films/search")
    public List<Film> searchFilm(@RequestParam("query") String query,
                                 @RequestParam("by") String titleAndDirector) {
        return filmService.searchFilm(query, titleAndDirector);
    }

    @GetMapping(value = "/films/common")
    public List<Film> findListOfCommonFilms(@RequestParam("userId") long userId,
                                            @RequestParam("friendId") long friendId) {
        return filmService.findListOfCommonFilms(userId, friendId);
    }
}
