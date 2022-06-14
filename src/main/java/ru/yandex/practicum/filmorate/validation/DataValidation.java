package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class DataValidation {
    private static final LocalDate BIRTHDAY_MOVIE = LocalDate.of(1895, 12, 28);

    public void userdataVerification(final User user) {
        String login = user.getLogin();
        if (login.contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }

        String name = user.getName();
        if (name == null || name.isBlank()) {
            user.setName(login);
        }
    }

    public void filmDataVerification(final Film film) {
        LocalDate date = film.getReleaseDate();
        if (date.isBefore(BIRTHDAY_MOVIE)) {
            throw new ValidationException(
                    "Дата релиза раньше 28 декабря 1895 года"
            );
        }
    }
}
