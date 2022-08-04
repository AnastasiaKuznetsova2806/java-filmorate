package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
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
        userStorage.addToFriends(id, friendId);
    }

    //Удаление из друзей
    public void unfriending(Long id, Long friendId) {
        checkId(id);
        checkId(friendId);
        userStorage.unfriending(id, friendId);
    }

    //Список пользователей, являющихся друзьями.
    public List<User> findFriendList(Long id) {
        checkId(id);

        Set<Long> friendListId = new HashSet<>(userStorage.findFriendList(id));
        return fillUsersList(friendListId);
    }

    //Вывод списка общих друзей
    public List<User> findListOfCommonFriends(Long id, Long otherId) {
        checkId(id);
        checkId(otherId);

        Set<Long> userSet = new HashSet<>(userStorage.findFriendList(id));
        Set<Long> friendSet = new HashSet<>(userStorage.findFriendList(otherId));

        Set<Long> friendSetId =  userSet.stream()
                .filter(friendSet::contains)
                .collect(Collectors.toSet());

        return fillUsersList(friendSetId);
    }

    private void checkId(Long id) {
        if (id == null) {
            throw new ValidationException("Поле пустое!");
        }
        findUserById(id);
    }

    private List<User> fillUsersList(Set<Long> friendListId) {
        return friendListId.stream()
                .map(this::findUserById)
                .collect(Collectors.toList());
    }

    //Удаление пользователя
    public void deleteUserById(Long userId) {
        userStorage.deleteUserById(userId);
    }

    //Получуние списка рекомендованных фильмов для пользователя
    public List<Film> recommendationsFilms(Long id) {
        checkId(id);
        List<Film> userFilms = new ArrayList<>(filmStorage.findAllFavoriteMovies(id));
        List<Film> recommendationsFilms = new ArrayList<>(filmStorage.recommendationsFilm(id));
        return recommendationsFilms.stream()
                .filter(film -> !userFilms.contains(film))
                .collect(Collectors.toList());
    }

}
