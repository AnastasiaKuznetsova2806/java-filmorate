package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.referencebook.Mpa;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    LocalDate date = LocalDate.of(2009, 1, 1);

    @BeforeEach
    public void setUp() {
        Film film = new Film("Аватар",
                "Научно-фантастический фильм",
                date,
                162,
                new Mpa(1, "G")
        );
        film.setId(1);
        filmStorage.createFilm(film);
    }

    @Test
    public void test1_createFilmAndFindFilmById() {
        Film filmResult = filmStorage.findFilmById(1L);

        assertEquals("Film(id=1, name=Аватар, description=Научно-фантастический фильм, " +
                "releaseDate=2009-01-01, duration=162, likes=[], mpa=Mpa(id=1, name=G), genres=[])",
                filmResult.toString());
    }

    @Test
    public void test2_updateFilm() {
        Film filmUpdate = new Film("Аватар_Update",
                "Научно-фантастический фильм",
                date,
                162,
                new Mpa(1, "G")
        );
        filmUpdate.setId(1);
        filmStorage.updateFilm(filmUpdate);
        Film filmResult = filmStorage.findFilmById(1L);

        assertEquals(filmUpdate, filmResult);
    }

    @Test
    public void test3_findAllFilms() {
        assertEquals("[Film(id=1, name=Аватар_Update, description=Научно-фантастический фильм, " +
                        "releaseDate=2009-01-01, duration=162, likes=[], mpa=Mpa(id=1, name=G), genres=[]), " +
                        "Film(id=2, name=Аватар, description=Научно-фантастический фильм, " +
                        "releaseDate=2009-01-01, duration=162, likes=[], mpa=Mpa(id=1, name=G), genres=[]), " +
                        "Film(id=3, name=Аватар, description=Научно-фантастический фильм, " +
                        "releaseDate=2009-01-01, duration=162, likes=[], mpa=Mpa(id=1, name=G), genres=[]), " +
                        "Film(id=4, name=Аватар, description=Научно-фантастический фильм, " +
                        "releaseDate=2009-01-01, duration=162, likes=[], mpa=Mpa(id=1, name=G), genres=[]), " +
                        "Film(id=5, name=Аватар, description=Научно-фантастический фильм, " +
                        "releaseDate=2009-01-01, duration=162, likes=[], mpa=Mpa(id=1, name=G), genres=[]), " +
                        "Film(id=6, name=Аватар, description=Научно-фантастический фильм, " +
                        "releaseDate=2009-01-01, duration=162, likes=[], mpa=Mpa(id=1, name=G), genres=[])]",
                filmStorage.findAllFilms().toString()
        );
    }

    @Test
    public void test4_addLikeForNonExistentUser() {
        final DataNotFoundException exception = assertThrows(DataNotFoundException.class, () ->
                filmStorage.addLike(1L, 1L));
        assertEquals(
                "Пользователь 1 не найден",
                exception.getMassage()
        );
    }

    @Test
    public void test5_addLikeAndDeletingLike() {
        User user = new User("email@mail.ru",
                "login",
                "name",
                date
        );
        userStorage.createUser(user);
        filmStorage.addLike(1L, 1L);
        Film filmResult = filmStorage.findFilmById(1L);

        assertTrue(filmResult.getLikes().contains(1L));

        filmStorage.deletingLike(1L, 1L);
        filmResult = filmStorage.findFilmById(1L);

        assertEquals(0, filmResult.getLikes().size());
    }

    @Test
    public void test6_deletingLikeForNonExistentUser() {
        final DataNotFoundException exception = assertThrows(DataNotFoundException.class, () ->
                filmStorage.deletingLike(1L, 1L));
        assertEquals(
                "Пользователь 1 не найден",
                exception.getMassage()
        );
    }
}