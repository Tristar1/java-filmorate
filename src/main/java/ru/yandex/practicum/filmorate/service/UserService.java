package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage){
        this.userStorage = userStorage;
    }

    public User addFriend(long idUser, long idFriend){
        User user = userStorage.getUser(idUser);
        user.getFriendList().add(userStorage.getUser(idFriend).getId());
        userStorage.getUser(idFriend).getFriendList().add(idUser);
        return user;
    }

    public User removeFriend(long idUser, long idFriend){
       User user = userStorage.getUser(idUser);
       user.getFriendList().remove(userStorage.getUser(idFriend).getId());
       userStorage.getUser(idFriend).getFriendList().remove(idUser);
       return user;
    }

    public List<User> getFriends(long idUser){

        return userStorage.getAll().stream()
                .filter((User user) -> userStorage.getUser(idUser).getFriendList().contains(user.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long idUser, long idFriend){

        List<Long> commonFriendsId = userStorage.getUser(idUser).getFriendList().stream()
                        .filter((Long id) -> userStorage.getUser(idFriend)
                        .getFriendList().contains(id))
                        .collect(Collectors.toList());

        return userStorage.getAll().stream()
                .filter((User user) -> commonFriendsId.contains(user.getId()))
                .collect(Collectors.toList());
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
        return userStorage.getUser(userId);
    }
}
