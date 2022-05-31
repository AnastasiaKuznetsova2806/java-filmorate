package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    private int increaseId() {
        return ++id;
    }

    @PostMapping(value = "/users")
    public User createUser( @Valid @RequestBody User user) {
        log.info("Получен запрос на добавление объекта: '{}'", user);

        dataCheck(user);
        user.setId(increaseId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на добавление объекта: '{}'", user);

        int id = user.getId();
        if(!users.containsKey(id)) {
            throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "Не найдена запись с id = " + id);
        }
        dataCheck(user);
        users.put(id, user);
        return user;
    }

    @GetMapping(value = "/users")
    public Collection<User> findAllUsers() {
        return users.values();
    }

    private void dataCheck(User user) {
        String login = user.getLogin();
        String name = user.getName();

        if (login.contains(" ")) {
            throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "Логин не может содержать пробелы");
        }

        if (name == null || name.isBlank()) {
            user.setName(login);
        }
    }
}
