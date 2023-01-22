package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserUnvaliableException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("inMemoryRealisation")
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users = new HashMap<>();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        user.setId(users.size() + 1);
        Validator.validateUser(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user){
        Validator.validateUser(user);
        user.setFriendList(getUser(user.getId()).get().getFriendList());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> getUser(Long userId){
        User user = users.get(userId);

        if (user == null) {
            throw new UserUnvaliableException("Не найден пользователь с ID = " + userId);
        }

        return Optional.of(user);
    }

    @Override
    public User addFriend(Long idUser, Long idFriend){
        User user = getUser(idUser).get();
        user.getFriendList().add(getUser(idFriend).get().getId());
        getUser(idFriend).get().getFriendList().add(idUser);
        return user;
    }

    @Override
    public User removeFriend(Long idUser, Long idFriend){
        User user = getUser(idUser).get();
        user.getFriendList().remove(getUser(idFriend).get().getId());
        getUser(idFriend).get().getFriendList().remove(idUser);
        return user;
    }

    @Override
    public List<User> getFriends(Long idUser){

        return getAll().stream()
                .filter((User user) -> getUser(idUser).get().getFriendList().contains(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(Long idUser, Long idFriend){

        List<Long> commonFriendsId = getUser(idUser).get().getFriendList().stream()
                .filter((Long id) -> getUser(idFriend)
                        .get().getFriendList().contains(id))
                .collect(Collectors.toList());

        return getAll().stream()
                .filter((User user) -> commonFriendsId.contains(user.getId()))
                .collect(Collectors.toList());
    }

}
