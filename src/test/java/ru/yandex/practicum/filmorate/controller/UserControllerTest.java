package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
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
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {
    private final LocalDate birthday = LocalDate.of(2013, 10, 28);
    private Validator validator;
    private UserStorage userStorage;

    @BeforeEach
    public void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void test1_createUserWithEmptyEmail() {
        User user = new User("", "login", "name", birthday);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.toString().contains("Электронная почта не может быть пустой"));
    }

    @Test
    public void test2_createUserWithDoesNotMatchEmail() {
        User user = new User("email_mail.ru", "login", "name", birthday);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.toString().contains("Электронная почта не соответствует формату электронного адреса"));
    }

    @Test
    public void test3_createUserWithEmptyLogin() {
        User user = new User("email@mail.ru", null, "name", birthday);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.toString().contains("Логин не может быть пустым"));
    }

    @Test
    public void test4_createUserWithFutureBirthday() {
        LocalDate futureDay = LocalDate.of(2023, 12,14);
        User user = new User("email@mail.ru", "login", "name", futureDay);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.toString().contains("Дата рождения не может быть в будущем"));
    }

    @Test
    public void test5_createUser() {
        User result = createUser();
        assertEquals(
                "User(id=1, email=email@mail.ru, login=login, " +
                        "name=name, birthday=2013-10-28, friends=[])",
                result.toString()
        );
    }

    @Test
    public void test6_updateUserWithNonExistentId() {
        String json = "{\n" +
                "  \"login\": \"login\",\n" +
                "  \"name\": \"name\",\n" +
                "  \"id\": -1,\n" +
                "  \"email\": \"email@yandex.ru\",\n" +
                "  \"birthday\": \"2013-10-28\"\n" +
                "}";
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        User user = gson.fromJson(json, User.class);

        final DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
        //    userStorage = new InMemoryUserStorage();
            userStorage.updateUser(user);
        });
        assertEquals(
                "Не найдена запись с id = -1",
                exception.getMassage()
        );
    }

    @Test
    public void test7_updateUser() {
        createUser();
        String json = "{\n" +
                "  \"login\": \"loginUpdate\",\n" +
                "  \"name\": \"name\",\n" +
                "  \"id\": 1,\n" +
                "  \"email\": \"email@yandex.ru\",\n" +
                "  \"birthday\": \"2013-10-28\"\n" +
                "}";
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        User user = gson.fromJson(json, User.class);

        User result = userStorage.updateUser(user);
        assertEquals(
                "User(id=1, email=email@yandex.ru, login=loginUpdate, " +
                        "name=name, birthday=2013-10-28, friends=null)",
                result.toString()
        );
    }

    @Test
    public void test8_findAllUsers() {
        createUser();
        Collection<User> users = userStorage.findAllUsers();
        System.out.println(users);
        assertEquals(
                "[User(id=1, email=email@mail.ru, login=login, " +
                        "name=name, birthday=2013-10-28, friends=[])]",
                users.toString()
        );
    }

    private User createUser() {
        User user = new User("email@mail.ru", "login", "name", birthday);
    //    userStorage = new InMemoryUserStorage();
        return userStorage.createUser(user);
    }
}