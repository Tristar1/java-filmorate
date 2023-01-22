package ru.yandex.practicum.filmorate.storage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserUnvaliableException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface UserStorage {

    List<User> getAll();
    User create(User user);
    User update(User user) ;
    Optional<User> getUser(Long userId);
    User addFriend(Long idUser, Long idFriend);
    User removeFriend(Long idUser, Long idFriend);
    List<User> getFriends(Long idUser);
    List<User> getCommonFriends(Long idUser, Long idFriend);

}
