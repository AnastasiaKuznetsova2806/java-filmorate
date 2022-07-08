package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users")
    public User createUser( @Valid @RequestBody User user) {
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
    public User findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @PutMapping(value = "users/{id}/friends/{friendId}")
    public void addingToFriends(@PathVariable Long id,
                                @PathVariable Long friendId) {
        log.info("Получен запрос на добавление объекта в друзья");
        userService.addToFriends(id, friendId);
    }

    @DeleteMapping(value = "users/{id}/friends/{friendId}")
    public void unfriending(@PathVariable Long id,
                            @PathVariable Long friendId) {
        log.info("Получен запрос на удаление объекта из друзей");
        userService.unfriending(id, friendId);
    }

    @GetMapping(value = "users/{id}/friends")
    public List<User> findFriendList(@PathVariable Long id) {
        return userService.findFriendList(id);
    }

    @GetMapping(value = "users/{id}/friends/common/{otherId}")
    public List<User> findListOfCommonFriends(@PathVariable Long id,
                                              @PathVariable Long otherId) {
        return userService.findListOfCommonFriends(id, otherId);
    }
}
