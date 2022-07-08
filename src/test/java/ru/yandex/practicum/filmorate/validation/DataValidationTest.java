package ru.yandex.practicum.filmorate.validation;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.referencebook.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DataValidationTest {
    private final LocalDate birthday = LocalDate.of(2013, 10, 28);
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Test
    public void test1_createUserWithLoginWithSpaces() {
        final ValidationException exception = assertThrows(ValidationException.class, () -> {
            User user = new User("email@mail.ru", "log in", "name", birthday);
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
            Film film = new Film("Аватар",
                    "Научно-фантастический фильм",
                    date,
                    162,
                    new Mpa(1)
            );
            filmStorage.createFilm(film);
        });
        assertEquals(
                "Дата релиза раньше 28 декабря 1895 года",
                exception.getMassage()
        );
    }
}