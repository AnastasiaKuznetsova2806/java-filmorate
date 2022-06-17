package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DataValidationTest {
    private final LocalDate birthday = LocalDate.of(2013, 10, 28);
    private InMemoryUserStorage userStorage;
    private InMemoryFilmStorage filmStorage;

    @Test
    public void test1_createUserWithLoginWithSpaces() {
        final ValidationException exception = assertThrows(ValidationException.class, () -> {
            User user = new User("email@mail.ru", "log in", "name", birthday);
            userStorage = new InMemoryUserStorage();
            userStorage.createUser(user);
        });
        assertEquals(
                "Логин не может содержать пробелы",
                exception.getMassage()
        );
    }

    @Test
    public void test2_createUserWithEmptyName() {
        User user = new User("email@mail.ru", "login", "", birthday);
        userStorage = new InMemoryUserStorage();
        User result = userStorage.createUser(user);
        assertEquals(
                "User(id=1, email=email@mail.ru, login=login, name=login, birthday=2013-10-28, friends=[])",
                result.toString()
        );
    }

    @Test
    public void test3_createFilmWithReleaseDateNoEarlier12_28_1895() {
        final LocalDate date = LocalDate.of(1009,1,1);
        final ValidationException exception = assertThrows(ValidationException.class, () -> {
            Film film = new Film("Аватар", "Научно-фантастический фильм", date, 162);
            filmStorage = new InMemoryFilmStorage();
            filmStorage.createFilm(film);
        });
        assertEquals(
                "Дата релиза раньше 28 декабря 1895 года",
                exception.getMassage()
        );
    }
}