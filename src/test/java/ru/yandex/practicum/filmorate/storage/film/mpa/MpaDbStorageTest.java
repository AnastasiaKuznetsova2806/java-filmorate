package ru.yandex.practicum.filmorate.storage.film.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.referencebook.Mpa;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {
    private final MpaStorage mpaStorage;

    @Test
    public void test1_findAllMpa() {
        Collection<Mpa> mpaResult = mpaStorage.findAllMpa();
        assertEquals("[Mpa(id=1, name=G), Mpa(id=2, name=PG), Mpa(id=3, name=PG-13), " +
                        "Mpa(id=4, name=R), Mpa(id=5, name=NC-17)]",
                mpaResult.toString()
        );
    }

    @Test
    public void test2_findMpaByIdForNonExistentMpa() {
        final DataNotFoundException exception = assertThrows(DataNotFoundException.class, () ->
                mpaStorage.findMpaById(8));
        assertEquals(
                "Рейтинг 8 не найден",
                exception.getMassage()
        );
    }

    @Test
    public void test3_findMpaById() {
        Mpa mpaResult = mpaStorage.findMpaById(1);
        assertEquals("Mpa(id=1, name=G)", mpaResult.toString());
    }
}