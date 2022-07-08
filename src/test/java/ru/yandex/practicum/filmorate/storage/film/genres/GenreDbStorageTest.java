package ru.yandex.practicum.filmorate.storage.film.genres;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.referencebook.Genre;
import ru.yandex.practicum.filmorate.model.referencebook.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {
    private final GenreStorage genreStorage;
    private final FilmStorage filmStorage;
    Film film;
    Genre genre;
    Set<Genre> genres = new HashSet<>();

    @BeforeEach
    public void setUp() {
        LocalDate date = LocalDate.of(2009, 1, 1);

        film = new Film("Аватар",
                "Научно-фантастический фильм",
                date,
                162,
                new Mpa(1, "G")
        );
        filmStorage.createFilm(film);

        genre = new Genre(1);
        genres.add(genre);
    }

    @Test
    public void test1_addGenreAndFindMovieGenre() {
        genreStorage.addGenre(1L, genres);
        List<Genre> genreResult = genreStorage.findMovieGenre(1L);

        assertEquals(1, genreResult.get(0).getId());
    }

    @Test
    public void test2_findAllGenres() {
        Collection<Genre> genresResult = genreStorage.findAllGenres();
        assertEquals("[Genre(id=1, name=Комедия), Genre(id=2, name=Драма), Genre(id=3, " +
                "name=Мультфильм), Genre(id=4, name=Триллер), Genre(id=5, name=Документальный), " +
                "Genre(id=6, name=Боевик)]",
                genresResult.toString()
        );
    }

    @Test
    public void test3_findGenreByIdForNonExistentGenre() {
        final DataNotFoundException exception = assertThrows(DataNotFoundException.class, () ->
                genreStorage.findGenreById(8));
        assertEquals(
                "Жанр 8 не найден",
                exception.getMassage()
        );
    }

    @Test
    public void test4_findGenreById() {
        Genre genresResult = genreStorage.findGenreById(1);
        assertEquals("Genre(id=1, name=Комедия)", genresResult.toString());
    }
}