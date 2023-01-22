package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsDao {

    User addFriend(Long userId, Long friendId);
    User removeFriend(Long userId, Long friendId);
    List<User> getFriends(Long userId);
    List<User> getCommonFriends(Long userId, Long friendId);

}
