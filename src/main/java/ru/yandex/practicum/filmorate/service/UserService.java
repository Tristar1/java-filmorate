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

    UserStorage userStorage;

    public UserService(UserStorage userStorage){
        this.userStorage = userStorage;
    }

    public void addFriend(long idUser, long idFriend){
        userStorage.getUser(idUser).getFriendList().add(userStorage.getUser(idFriend).getId());
        userStorage.getUser(idFriend).getFriendList().add(idUser);
    }

    public void removeFriend(long idUser, long idFriend){
       userStorage.getUser(idUser).getFriendList().remove(userStorage.getUser(idFriend).getId());
       userStorage.getUser(idFriend).getFriendList().remove(idUser);
    }

    public List<User> getFriends(long idUser){

        return userStorage.getAll().stream()
                .filter((User user) -> {return userStorage.getUser(idUser).getFriendList().contains(user.getId());})
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long idUser, long idFriend){

        List<Long> commonFriendsId = userStorage.getUser(idUser).getFriendList().stream()
                        .filter((Long id) -> {return userStorage.getUser(idFriend)
                        .getFriendList().contains(id);})
                        .collect(Collectors.toList());

        return userStorage.getAll().stream()
                .filter((User user) -> {return commonFriendsId.contains(user.getId());})
                .collect(Collectors.toList());
    }



}
