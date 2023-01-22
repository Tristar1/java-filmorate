package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserUnvaliableException;

import java.util.*;

@Component
@Qualifier("dbRealisation")
public class UserDaoImpl implements UserDao {

    private final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> getUserById(Long userId){
        Optional<User> user = getUsers(userId);
        if (user.isEmpty()) {
            throw new UserUnvaliableException("Не найден пользователь с ID = " + userId);
        }
        return user;
    }

    @Override
    public List<User> getUsers(List<Long> usersId){
        String parameters = String.join(",", Collections.nCopies(usersId.size(), "?"));
        String usersQuery = String.format(getSeveralUserText(),parameters);
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(usersQuery, usersId.toArray());
        return usersFromRowSet(userRows);
    }

    public Optional<User> getUsers(Long usersId){
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(getOneUserText(),usersId);
        return userFromRowSet(userRows);
    }

    public List<User> getUsers(){
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(getUsersText());
        return usersFromRowSet(userRows);
    }

    @Override
    public Optional<User> getUserByEmail(String email){
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(getUserByEmailText(), email);
        return userFromRowSet(userRows);
    }

    private String getSeveralUserText(){
        return "select * from users where id IN (%s)";
    }

    private String getOneUserText(){
        return "select * from users where id = ?";
    }

    private String getUserByEmailText(){
        return "select * from users where email = ?";
    }

    private String getUsersText(){
        return "select * from users";
    }

    private Optional<User> userFromRowSet(SqlRowSet sqlRowSet) {
        List<User> userList = usersFromRowSet(sqlRowSet);
        return (userList.isEmpty()) ? Optional.empty()
                : Optional.of(userList.get(0));
    }

    private List<User> usersFromRowSet(SqlRowSet sqlRowSet) {

        List<User> userList = new ArrayList<>();

        while (sqlRowSet.next()) {
            User user = User.builder()
                    .id(sqlRowSet.getLong("id"))
                    .email(sqlRowSet.getString("email"))
                    .login(sqlRowSet.getString("login"))
                    .name(sqlRowSet.getString("name"))
                    .birthday(Objects.requireNonNull(sqlRowSet.getDate("birthday")).toLocalDate())
                    .build();

            log.info("Найден пользователь: {} {}", user.getId(), user.getName());
            userList.add(user);
        }
        return userList;
    }
}
