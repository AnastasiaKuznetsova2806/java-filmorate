package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    //Создать пользователя
    User createUser(User user);

    //Обновить пользователя
    User updateUser(User user);

    //Получить список всех пользователей
    Collection<User> findAllUsers();

    //Получить пользователя по уникальному идентификатору
    User findUserById(long id);

    //Добавить в друзья
    void addToFriends(long id, long friendId);

    //Удалить из друзей
    void unfriending(long id, long friendId);

    //Получить список пользователей, являющихся друзьями.
    List<Long> findFriendList(long id);

    //Удалить пользователя
    void deleteUserById(long userId);
}
