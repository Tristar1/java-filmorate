package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;
import java.util.List;
import java.util.Optional;

@Component
public class FriendsDaoImpl implements FriendsDao {

    private final Logger log = LoggerFactory.getLogger(FriendsDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;

    public FriendsDaoImpl(JdbcTemplate jdbcTemplate, UserDao userDao){

        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        Optional<User> user = userDao.getUserById(userId);
        Optional<User> friend = userDao.getUserById(friendId);

        if (friendsAlreadyExist(userId,friendId)) {
            return user.get();
        }

        jdbcTemplate.update(addFriendText(),
                userId,friendId);
        return user.get();
    }

    @Override
    public User removeFriend(Long userId, Long friendId){
        Optional<User> user = userDao.getUserById(userId);
        jdbcTemplate.update(removeFriendText(),userId,friendId);
        return user.get();
    }

    @Override
    public List<User> getFriends(Long userId){
        Optional<User> user = userDao.getUserById(userId);
        List<Long> idArray = jdbcTemplate.queryForList(getFriendText(), Long.class, userId);
        return userDao.getUsers(idArray);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId){
        Optional<User> user = userDao.getUserById(userId);
        List<Long> idArray = jdbcTemplate.queryForList(getCommonFriends(), Long.class, userId, friendId);
        return userDao.getUsers(idArray);
    }

    private boolean friendsAlreadyExist(Long userId, Long friendId){
        SqlRowSet friendsRow = jdbcTemplate.queryForRowSet(validateFriendlistText(), userId, friendId);
        return friendsRow.next();
    }

    private String validateFriendlistText(){
        return "SELECT * FROM friends WHERE user_id = ? AND friend_id = ?";
    }

    private String addFriendText(){
        return "INSERT INTO FRIENDS (user_id, friend_id) " +
                "VALUES \n" +
                "(?,?)";
    }

    private String removeFriendText(){
        return "DELETE FRIENDS WHERE user_id = ? AND friend_id = ?";
    }

    private String getFriendText(){
        return "SELECT friend_id FROM FRIENDS WHERE user_id = ?";
    }

    private String getCommonFriends(){
        return "SELECT f.FRIEND_ID \n" +
                "FROM FRIENDS AS F \n" +
                "INNER JOIN (SELECT f2.FRIEND_ID AS ff FROM FRIENDS f2 WHERE f2.USER_ID = ?)AS CF \n" +
                "ON (F.FRIEND_ID = CF.ff) \n" +
                "WHERE F.USER_ID  = ?";
    }

}
