package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final InMemoryFilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public InMemoryFilmStorage getFilmStorage() {
        return filmStorage;
    }

    //Добавление лайка
    public void addingLike(Long id, Long userId) {
        checkId(id);
        checkId(userId);

        filmStorage.getFilms().get(id).getLikes().add(userId);
    }

    //Удаление лайка
    public void deletingLike(Long id, Long userId) {
        checkId(id);
        checkId(userId);

        if (!filmStorage.getFilms().get(id).getLikes().contains(userId)) {
            throw new DataNotFoundException(String.format("Лайк с № %d не найден", userId));
        }
        filmStorage.getFilms().get(id).getLikes().remove(userId);
    }

    //вывод наиболее популярных фильмов
    public List<Film> listOfPopularMovies(int count) {
        if (count <= 0) {
            throw new ValidationException("Поле count должно быть больше 0");
        }

        return filmStorage.getFilms().values().stream()
                .sorted((f0, f1) -> f1.getLikes().size() - f0.getLikes().size())
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
