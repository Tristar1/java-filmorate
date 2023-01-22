package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Qualifier("dbRealisation") UserStorage userStorage){
        this.userStorage = userStorage;
    }

    public User addFriend(long idUser, long idFriend){
        return userStorage.addFriend(idUser, idFriend);
    }

    public User removeFriend(long idUser, long idFriend){
       return userStorage.removeFriend(idUser, idFriend);
    }

    public List<User> getFriends(long idUser){
        return userStorage.getFriends(idUser);
    }

    public List<User> getCommonFriends(long idUser, long idFriend){
        return userStorage.getCommonFriends(idUser, idFriend);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }
    public User create(User user) {
        return userStorage.create(user);
    }
    public User update(User user) {
        return userStorage.update(user);
    }
    public User getUser(long userId) {
        return userStorage.getUser(userId).get();
    }
}
