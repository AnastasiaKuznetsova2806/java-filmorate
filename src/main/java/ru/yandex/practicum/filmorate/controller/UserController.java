package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.referencebook.Feed;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users")
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на добавление объекта: '{}'", user);
        return userService.createUser(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на обновление объекта: '{}'", user);
        return userService.updateUser(user);
    }

    @GetMapping(value = "/users")
    public Collection<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping(value = "/users/{id}")
    public User findUserById(@PathVariable long id) {
        return userService.findUserById(id);
    }

    @PutMapping(value = "users/{id}/friends/{friendId}")
    public void addingToFriends(@PathVariable long id,
                                @PathVariable long friendId) {
        log.info("Получен запрос на добавление объекта в друзья");
        userService.addToFriends(id, friendId);
    }

    @DeleteMapping(value = "users/{id}/friends/{friendId}")
    public void unfriending(@PathVariable long id,
                            @PathVariable long friendId) {
        log.info("Получен запрос на удаление объекта из друзей");
        userService.unfriending(id, friendId);
    }

    @GetMapping(value = "users/{id}/friends")
    public List<User> findFriendList(@PathVariable long id) {
        return userService.findFriendList(id);
    }

    @GetMapping(value = "users/{id}/friends/common/{otherId}")
    public List<User> findListOfCommonFriends(@PathVariable long id,
                                              @PathVariable long otherId) {
        return userService.findListOfCommonFriends(id, otherId);
    }

    @DeleteMapping(value = "users/{userId}")
    public void deleteUserById(@PathVariable long userId) {
        log.info("Получен запрос на удаление пользователя '{}'", userId);
        userService.deleteUserById(userId);
    }

    @GetMapping(value = "users/{id}/recommendations")
    public List<Film> recommendationsFilm(@PathVariable long id) {
        return userService.recommendationsFilms(id);
    }

    @GetMapping(value = "/users/{id}/feed")
    public List<Feed> findAllFeedById(@PathVariable long id) {
        return userService.findAllFeedById(id);
    }
}
