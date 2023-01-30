package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Optional;

@Component
@Qualifier("dbRealisation")
public class LikesDaoImpl implements LikesDao {

    private final Logger log = LoggerFactory.getLogger(FilmDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;
    private final FilmDao filmDao;

    public LikesDaoImpl(JdbcTemplate jdbcTemplate, UserDao userDao, FilmDao filmDao){
        this.jdbcTemplate=jdbcTemplate;
        this.userDao = userDao;
        this.filmDao = filmDao;
    }

    @Override
    public Film addLike(Integer filmId, Long userId){
        Optional<Film> film = filmDao.getFilmById(filmId);
        userDao.getUserById(userId);
        if (!likeAlreadyExist(filmId, userId)){
            jdbcTemplate.update(addLikeText(),filmId,userId);
        }

        return film.orElseThrow();
    }

    @Override
    public Film removeLike(Integer filmId, Long userId){
        Optional<Film> film = filmDao.getFilmById(filmId);
        userDao.getUserById(userId);
        if (likeAlreadyExist(filmId, userId)){
            jdbcTemplate.update(removeLikeText(),filmId,userId);
        }

        return film.orElseThrow();
    }

    private boolean likeAlreadyExist(Integer filmId, Long userId){
        SqlRowSet likesRowSet = jdbcTemplate.queryForRowSet(getLikesByFilmUserText(),filmId,userId);
        return likesRowSet.next();
    }

    private String addLikeText(){
        return "INSERT INTO LIKES (FILM_ID, USER_ID) " +
                "VALUES \n" +
                "(?,?)";
    }

    private String removeLikeText(){
        return "DELETE FROM LIKES WHERE FILM_ID =? AND USER_ID = ?";
    }

    private String getLikesByFilmUserText(){
        return "SELECT *  FROM LIKES WHERE FILM_ID = ? AND USER_ID =?";
    }
}
