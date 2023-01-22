package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> getUserById(Long userId);
    Optional<User> getUserByEmail(String email);

    List<User> getUsers(List<Long> usersId);

    List<User> getUsers();

}
