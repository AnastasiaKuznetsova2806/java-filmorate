package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //Создание пользователя
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    //Обновление пользователя
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    //Получение списка всех пользователей
    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    //Получение пользователя по уникальному идентификатору
    public User findUserById(Long id) {
        return userStorage.findUserById(id);
    }

    //Добавление в друзья
    public void addToFriends(Long id, Long friendId) {
        checkId(id);
        checkId(friendId);

        User user = findUserById(id);
        user.getFriends().add(friendId);

        User friend = findUserById(friendId);
        friend.getFriends().add(id);
    }

    //Удаление из друзей
    public void unfriending(Long id, Long friendId) {
        checkId(id);
        checkId(friendId);

        User user = findUserById(id);
        user.getFriends().remove(friendId);

        User friend = findUserById(friendId);
        friend.getFriends().remove(id);
    }

    //Список пользователей, являющихся друзьями.
    public List<User> findFriendList(Long id) {
        checkId(id);

        User user = findUserById(id);
        Set<Long> friendListId = user.getFriends();
        return fillUsersList(friendListId);
    }

    //Вывод списка общих друзей
    public List<User> findListOfCommonFriends(Long id, Long otherId) {
        checkId(id);
        checkId(otherId);

        User user = findUserById(id);
        Set<Long> userSet = user.getFriends();

        User friend = findUserById(otherId);
        Set<Long> friendSet = friend.getFriends();

        Set<Long> friendSetId =  userSet.stream()
                .filter(friendSet::contains)
                .collect(Collectors.toSet());

        return fillUsersList(friendSetId);
    }

    private void checkId(Long id) {
        if (id == null) {
            throw new ValidationException("Поле пустое!");
        }
        if (!userStorage.getUsers().containsKey(id)) {
            throw new DataNotFoundException(String.format("Пользователь %d не найден", id));
        }
    }

    private List<User> fillUsersList(Set<Long> friendListId) {
        List<User> friendsList = new ArrayList<>();

        friendListId.forEach(
                idFriend -> friendsList.add(userStorage.findUserById(idFriend))
        );
        return friendsList;
    }
}
