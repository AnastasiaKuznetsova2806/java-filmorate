package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.referencebook.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmControllerTest {
    private final LocalDate dateRelease = LocalDate.of(2009, 1, 1);
    private final Mpa mpa = new Mpa(1);
    private Validator validator;
    private final FilmStorage filmStorage;

    @BeforeEach
    public void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void test1_createFilmWithEmptyTitle() {
        Film film = new Film("", "Научно-фантастический фильм", dateRelease, 162, mpa);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.toString().contains("Имя не может быть пустым"));
    }

    @Test
    public void test2_createFilmWithDescriptionOfMore200Characters() {
        Film film = new Film("Аватар",
                "Научно-фантастический фильм 2009 года сценариста и режиссёра Джеймса Кэмерона " +
                        "с Сэмом Уортингтоном и Зои Салданой в главных ролях. Действие фильма происходит в " +
                        "2154 году, когда человечество добывает ценный минерал анобтаниум на Пандоре, обитаемом " +
                        "спутнике газовой планеты в звёздной системе Альфы Центавра.",
                dateRelease,
                162,
                mpa);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.toString().contains("Длина описания не должна быть более 200 символов"));
    }

    @Test
    public void test3_createFilmWithNegativeDuration() {
        Film film = new Film("Аватар", "Научно-фантастический фильм", dateRelease, -162, mpa);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.toString().contains("Продолжительность фильма должна быть положительной"));
    }

    @Test
    public void test4_updateFilmWithNonExistentId() {
        Film film = new Film("Аватар", "Научно-фантастический фильм", dateRelease, 162, mpa);
        film.setId(-1);

        final DataNotFoundException exception = assertThrows(DataNotFoundException.class, () ->
            filmStorage.updateFilm(film));
        assertEquals(
                "Фильм -1 не найден",
                exception.getMassage()
        );
    }
}