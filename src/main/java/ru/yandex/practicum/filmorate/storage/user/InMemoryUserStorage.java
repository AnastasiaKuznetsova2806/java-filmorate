package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.DataValidation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage{
    private final Map<Long, User> users = new HashMap<>();
    private final DataValidation dataValidation = new DataValidation();
    private long id = 0;

    private long increaseId() {
        return ++id;
    }

    public Map<Long, User> getUsers() {
        return users;
    }

    @Override
    public User createUser(User user) {
        dataValidation.userdataVerification(user);
        user.setId(increaseId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        long id = user.getId();
        if(!users.containsKey(id)) {
            throw new DataNotFoundException("Не найдена запись с id = " + id);
        }
        dataValidation.userdataVerification(user);
        users.put(id, user);
        return user;
    }

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public User findUserById(Long id) {
        if (id == null) {
            return null;
        }
        if (!users.containsKey(id)) {
            throw new DataNotFoundException(String.format("Пользователь %d не найден", id));
        }
        return users.get(id);
    }
}
