package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.ValidationUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserUnvaliableException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users = new HashMap<>();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        user.setId(users.size() + 1);
        validateUser(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        validateUser(user);
        user.setFriendList(getUser(user.getId()).getFriendList());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUser(long userId) {
        User user = users.get(userId);

        if (user == null) {
            throw new UserUnvaliableException("Не найден пользователь с ID = " + userId);
        }

        return user;
    }

    public void validateUser(User user) {

        String errorMessage;

        if (user.getFriendList() == null) {
            user.setFriendList(new HashSet<Long>());
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя пользователя заменено на логин");
            user.setName(user.getLogin());
        }

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            errorMessage = "Почта не может быть пустой и должна включать символ @";
            log.debug(errorMessage);
            throw new ValidationUserException(errorMessage);
        }

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            errorMessage = "Логин не может быть пустым и содержать пробелы!";
            log.debug(errorMessage);
            throw new ValidationUserException(errorMessage);
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            errorMessage = "Дата рождения пользователя не может быть больше текущей!";
            log.debug(errorMessage);
            throw new ValidationUserException(errorMessage);
        }

    }
}
