package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DataValidationTest {
    private final LocalDate birthday = LocalDate.of(2013, 10, 28);
    private UserController userController;
    private FilmController filmController;

    @Test
    public void test1_createUserWithLoginWithSpaces() {
        final ValidationException exception = assertThrows(ValidationException.class, () -> {
            User user = new User("email@mail.ru", "log in", "name", birthday);
            userController = new UserController();
            userController.createUser(user);
        });
        assertEquals(
                "500 INTERNAL_SERVER_ERROR \"Логин не может содержать пробелы\"",
                exception.getMassage()
        );
    }

    @Test
    public void test2_createUserWithEmptyName() {
        User user = new User("email@mail.ru", "login", "", birthday);
        userController = new UserController();
        User result = userController.createUser(user);
        assertEquals(
                "User(id=1, email=email@mail.ru, login=login, name=login, birthday=2013-10-28)",
                result.toString()
        );
    }

    @Test
    public void test3_createFilmWithReleaseDateNoEarlier12_28_1895() {
        final LocalDate date = LocalDate.of(1009,1,1);
        final ValidationException exception = assertThrows(ValidationException.class, () -> {
            Film film = new Film("Аватар", "Научно-фантастический фильм", date, 162);
            filmController = new FilmController();
            filmController.createFilm(film);
        });
        assertEquals(
                "500 INTERNAL_SERVER_ERROR \"Дата релиза раньше 28 декабря 1895 года\"",
                exception.getMassage()
        );
    }
}