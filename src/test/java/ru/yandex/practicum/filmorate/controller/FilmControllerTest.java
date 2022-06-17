package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.util.LocalDateAdapter;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    private final LocalDate dateRelease = LocalDate.of(2009, 1, 1);
    private Validator validator;
    private InMemoryFilmStorage filmStorage;

    @BeforeEach
    public void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void test1_createFilmWithEmptyTitle() {
        Film film = new Film("", "Научно-фантастический фильм", dateRelease, 162);
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
                162);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.toString().contains("Длина описания не должна быть более 200 символов"));
    }

    @Test
    public void test3_createFilmWithNegativeDuration() {
        Film film = new Film("Аватар", "Научно-фантастический фильм", dateRelease, -162);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.toString().contains("Продолжительность фильма должна быть положительной"));
    }

    @Test
    public void test4_createFilm() {
        Film result = createFilm();
        assertEquals(
                "Film(id=1, name=Аватар, description=Научно-фантастический фильм, " +
                        "releaseDate=2009-01-01, duration=162, likes=[])",
                result.toString()
        );
    }

    @Test
    public void test5_updateFilmWithNonExistentId() {
        String json = "{\n" +
                        "  \"id\": -1,\n" +
                        "  \"name\": \"Аватар Updated\",\n" +
                        "  \"releaseDate\": \"2009-01-01\",\n" +
                        "  \"description\": \"Научно-фантастический фильм\",\n" +
                        "  \"duration\": 162,\n" +
                        "  \"rate\": 4\n" +
                        "}";
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        Film film = gson.fromJson(json, Film.class);

        final DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
            filmStorage = new InMemoryFilmStorage();
            filmStorage.updateFilm(film);
        });
        assertEquals(
                "Не найдена запись с id = -1",
                exception.getMassage()
        );
    }

    @Test
    public void test6_updateFilm() {
        createFilm();
        String json = "{\n" +
                "  \"id\": 1,\n" +
                "  \"name\": \"Аватар Updated\",\n" +
                "  \"releaseDate\": \"2009-01-01\",\n" +
                "  \"description\": \"Научно-фантастический фильм\",\n" +
                "  \"duration\": 162,\n" +
                "  \"rate\": 4\n" +
                "}";
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        Film film = gson.fromJson(json, Film.class);

        Film result = filmStorage.updateFilm(film);
        assertEquals(
                "Film(id=1, name=Аватар Updated, description=Научно-фантастический фильм, " +
                        "releaseDate=2009-01-01, duration=162, likes=null)",
                result.toString()
        );
    }

    @Test
    public void test7_findAllFilms() {
        createFilm();
        Collection<Film> films = filmStorage.findAllFilms();
        assertEquals(
                "[Film(id=1, name=Аватар, description=Научно-фантастический фильм, " +
                        "releaseDate=2009-01-01, duration=162, likes=[])]",
                films.toString()
        );
    }

    private Film createFilm() {
        Film film = new Film("Аватар", "Научно-фантастический фильм", dateRelease, 162);
        filmStorage = new InMemoryFilmStorage();
        return filmStorage.createFilm(film);
    }
}