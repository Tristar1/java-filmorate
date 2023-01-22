package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.service.UserUnvaliableException;
import java.util.*;

@Slf4j
@Component
@Primary
@Qualifier("dbRealisation")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;
    private final FriendsDao friendsDao;

    public UserDbStorage(JdbcTemplate jdbcTemplate, UserDao userDao, FriendsDao friendsDao){
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
        this.friendsDao = friendsDao;
        clearAlltables();
    }

    private void clearAlltables(){
        String[] tables = {"USERS","FILMS","FILMS_GENRES","FRIENDS","LIKES"};
        for (String table : tables) {
            jdbcTemplate.execute("DELETE FROM " + table);
        }
    }

    @Override
    public Optional<User> getUser(Long userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public List<User> getAll() {
        return userDao.getUsers();
    }

    @Override
    public User create(User user) {
        user.setId(getAll().size() + 1);
        Validator.validateUser(user);
        if (userDao.getUserByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("Пользователь с почтой " + user.getEmail() + " уже существует!");
        }
        jdbcTemplate.update(insertUserText(),
                user.getId(),user.getEmail(),
                user.getLogin(),user.getName(),user.getBirthday());
        return userDao.getUserById(user.getId()).get();
    }

    @Override
    public User update(User user) throws UserUnvaliableException {
        Validator.validateUser(user);
        userDao.getUserById(user.getId());
        jdbcTemplate.update(updateUserText(),
                user.getEmail(),
                user.getLogin(),user.getName(),user.getBirthday(),user.getId());
        return userDao.getUserById(user.getId()).get();
    }

    @Override
    public User addFriend(Long userId, Long friendId){
        return friendsDao.addFriend(userId, friendId);
    }

    @Override
    public User removeFriend(Long userId, Long friendId){
        return friendsDao.removeFriend(userId, friendId);
    }

    @Override
    public List<User> getFriends(Long userId){
        return friendsDao.getFriends(userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId){
        return friendsDao.getCommonFriends(userId,friendId);
    }

    private String addFriendText(){
        return "INSERT INTO FRIENDS (user_id, friend_id) " +
                "VALUES \n" +
                "(?,?)";
    }

    private String insertUserText(){
        return "INSERT INTO USERS (id, email, login, name, birthday) " +
                "VALUES \n" +
                "(?,?,?,?,?)";
    }

    private String updateUserText(){
        return "UPDATE USERS SET \n" +
                "email = ?, login = ?, name = ?, birthday = ? \n" +
                "WHERE id = ?";
    }

    private String getFriendListText(){
        return "SELECT \n" +
                "f.friend_id AS f_id\n" +
                "FROM FRIENDS f \n" +
                "WHERE FRIEND_STATE > 0  AND f.FRIEND_ID = ? \n" +
                "UNION \n" +
                "SELECT \n" +
                "u.user_id  \n" +
                "FROM FRIENDS u \n" +
                "WHERE u.USER_STATE > 0 AND u.USER_ID = ?";
    }

}
