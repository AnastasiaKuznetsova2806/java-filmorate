package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    //Создать пользователя
    User createUser(User user);

    //Обновить пользователя
    User updateUser(User user);

    //Получить список всех пользователей
    Collection<User> findAllUsers();

    //Получить пользователя по уникальному идентификатору
    User findUserById(Long id);
}
