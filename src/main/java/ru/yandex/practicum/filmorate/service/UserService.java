package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.ValidationException;
import java.util.ArrayList;
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

    public InMemoryUserStorage getUserStorage() {
        return userStorage;
    }

    //добавление в друзья
    public void addingToFriends(Long id, Long friendId) {
        checkId(id);
        checkId(friendId);

        userStorage.getUsers().get(id).getFriends().add(friendId);
        userStorage.getUsers().get(friendId).getFriends().add(id);
    }

    //удаление из друзей
    public void unfriending(Long id, Long friendId) {
        checkId(id);
        checkId(friendId);

        userStorage.getUsers().get(id).getFriends().remove(friendId);
        userStorage.getUsers().get(friendId).getFriends().remove(id);
    }

    //список пользователей, являющихся друзьями.
    public List<User> findFriendList(Long id) {
        checkId(id);
        List<User> friendsList = new ArrayList<>();

        Set<Long> friendListId = userStorage.getUsers().get(id).getFriends();
        for (Long idFriend : friendListId) {
            friendsList.add(userStorage.findUserById(idFriend));
        }
        return friendsList;
    }

    //вывод списка общих друзей
    public List<User> findListOfCommonFriends(Long id, Long otherId) {
        checkId(id);
        checkId(otherId);

        Set<Long> user = userStorage.getUsers().get(id).getFriends();
        Set<Long> friend = userStorage.getUsers().get(otherId).getFriends();

        List<Long> friendListId =  user.stream()
                .filter(friend::contains)
                .collect(Collectors.toList());

        List<User> friendsList = new ArrayList<>();
        for (Long idFriend : friendListId) {
            friendsList.add(userStorage.findUserById(idFriend));
        }
        return friendsList;
    }

    private void checkId(Long id) {
        if (id == null) {
            throw new ValidationException("Поле пустое!");
        }
        if (!userStorage.getUsers().containsKey(id)) {
            throw new DataNotFoundException(String.format("Пользователь %d не найден", id));
        }
    }
}
