package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.referencebook.Feed;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.feed.FeedDbStorage;

import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final FeedDbStorage feedStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       FilmStorage filmStorage,
                       FeedDbStorage feedStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.feedStorage = feedStorage;
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
    public User findUserById(long id) {
        return userStorage.findUserById(id);
    }

    //Добавление в друзья
    public void addToFriends(long id, long friendId) {
        checkId(id);
        checkId(friendId);
        userStorage.addToFriends(id, friendId);

        Feed feed = new Feed(id, "FRIEND", "ADD", friendId);
        feedStorage.createFeed(feed);
    }

    //Удаление из друзей
    public void unfriending(long id, long friendId) {
        checkId(id);
        checkId(friendId);
        userStorage.unfriending(id, friendId);

        Feed feed = new Feed(id, "FRIEND", "REMOVE", friendId);
        feedStorage.createFeed(feed);
    }

    //Список пользователей, являющихся друзьями.
    public List<User> findFriendList(long id) {
        checkId(id);

        Set<Long> friendListId = new HashSet<>(userStorage.findFriendList(id));
        return fillUsersList(friendListId);
    }

    //Вывод списка общих друзей
    public List<User> findListOfCommonFriends(long id, long otherId) {
        checkId(id);
        checkId(otherId);

        Set<Long> userSet = new HashSet<>(userStorage.findFriendList(id));
        Set<Long> friendSet = new HashSet<>(userStorage.findFriendList(otherId));

        Set<Long> friendSetId =  userSet.stream()
                .filter(friendSet::contains)
                .collect(Collectors.toSet());

        return fillUsersList(friendSetId);
    }

    //Удаление пользователя
    public void deleteUserById(long userId) {
        userStorage.deleteUserById(userId);
    }

    //Получуние списка рекомендованных фильмов для пользователя
    public List<Film> recommendationsFilms(long id) {
        checkId(id);
        List<Film> userFilms = new ArrayList<>(filmStorage.findAllFavoriteMovies(id));
        List<Film> recommendationsFilms = new ArrayList<>(filmStorage.recommendationsFilm(id));
        return recommendationsFilms.stream()
                .filter(film -> !userFilms.contains(film))
                .collect(Collectors.toList());
    }

    //Получуние ленты событий пользователя
    public List<Feed> findAllFeedById(long id) {
        return feedStorage.findAllFeedById(id);
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
}
