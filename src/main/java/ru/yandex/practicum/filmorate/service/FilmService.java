package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final InMemoryFilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    //Создание фильма
    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    //Обновление фильма
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    //Получение списка всех фильмов
    public Collection<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    //Получение фильма по уникальному идентификатору
    public Film findFilmById(Long id) {
        return filmStorage.findFilmById(id);
    }

    //Добавление лайка
    public void addLike(Long id, Long userId) {
        checkId(id);
        checkId(userId);

        Film film = findFilmById(id);
        film.getLikes().add(userId);
    }

    //Удаление лайка
    public void deletingLike(Long id, Long userId) {
        checkId(id);
        checkId(userId);

        Film film = findFilmById(id);
        if (!film.getLikes().contains(userId)) {
            throw new DataNotFoundException(String.format("Лайк с № %d не найден", userId));
        }
        film.getLikes().remove(userId);
    }

    //Вывод наиболее популярных фильмов
    public List<Film> listOfPopularMovies(int count) {
        if (count <= 0) {
            throw new ValidationException("Поле count должно быть больше 0");
        }

        return filmStorage.getFilms().values().stream()
                .sorted()
                .limit(count)
                .collect(Collectors.toList());
    }

    private void checkId(Long id) {
        if (id == null) {
            throw new ValidationException("Поле id пустое!");
        }
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new DataNotFoundException(String.format("Фильм %d не найден", id));
        }
    }
}
