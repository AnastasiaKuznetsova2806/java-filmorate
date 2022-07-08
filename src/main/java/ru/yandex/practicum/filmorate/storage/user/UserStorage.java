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
    User findUserById(Long id);

    //Добавление в друзья
    void addToFriends(Long id, Long friendId);

    //Удаление из друзей
    void unfriending(Long id, Long friendId);

    //Список пользователей, являющихся друзьями.
    List<Long> findFriendList(Long id);
}
