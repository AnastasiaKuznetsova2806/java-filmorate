package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserStorage userStorage;
    LocalDate date = LocalDate.of(2013, 10, 28);

    @BeforeEach
    public void setUp() {
        addUser(1L);
    }

    @Test
    public void test1_createUserAndFindUserById() {
        User userResult = userStorage.findUserById(1L);
        assertEquals("User(id=1, email=email@mail.ru, login=login, name=name, " +
                "birthday=2013-10-28, friends=[])",
                userResult.toString()
        );
    }

    @Test
    public void test2_updateUser() {
        User userUpdate = new User("email@mail.ru","loginUpdate","name", date);
        userUpdate.setId(1);
        userStorage.updateUser(userUpdate);
        User userResult = userStorage.findUserById(1L);

        assertEquals(userUpdate, userResult);
    }

    @Test
    public void test3_findAllUsers() {
        assertEquals("[User(id=1, email=email@mail.ru, login=loginUpdate, name=name, " +
                        "birthday=2013-10-28, friends=[]), " +
                        "User(id=2, email=email@mail.ru, login=login, name=name, " +
                        "birthday=2013-10-28, friends=[]), " +
                        "User(id=3, email=email@mail.ru, login=login, name=name, " +
                        "birthday=2013-10-28, friends=[]), " +
                        "User(id=4, email=email@mail.ru, login=login, name=name, " +
                        "birthday=2013-10-28, friends=[])]",
                userStorage.findAllUsers().toString()
        );
    }

    @Test
    public void test4_findUserByIdForNonExistentUser() {
        final DataNotFoundException exception = assertThrows(DataNotFoundException.class, () ->
                userStorage.findUserById(10L));
        assertEquals(
                "Пользователь 10 не найден",
                exception.getMassage()
        );
    }

    @Test
    public void test5_addToFriendsForNonExistentUser() {
        final DataNotFoundException exception = assertThrows(DataNotFoundException.class, () ->
                userStorage.addToFriends(1L, 10L));
        assertEquals(
                "Пользователь 10 не найден",
                exception.getMassage()
        );
    }

    @Test
    public void test6_addToFriendsAndFindFriendListAndUnfriending() {
        addUser(2);
        userStorage.addToFriends(1L, 2L);
        List<Long> userResult = userStorage.findFriendList(1L);

        assertEquals(2, userResult.get(0));

        userStorage.unfriending(1L, 2L);
        userResult = userStorage.findFriendList(1L);

        assertEquals(0, userResult.size());
    }

    @Test
    public void test7_unfriendingForNonExistentUser() {
        final DataNotFoundException exception = assertThrows(DataNotFoundException.class, () ->
                userStorage.unfriending(1L, 2L));
        assertEquals(
                "Пользователь 2 не найден",
                exception.getMassage()
        );
    }

    private void addUser(long id) {
        User user = new User("email@mail.ru", "login", "name", date);
        user.setId(id);
        userStorage.createUser(user);
    }
}