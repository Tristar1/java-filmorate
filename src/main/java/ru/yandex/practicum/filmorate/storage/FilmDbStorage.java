package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Primary
@Qualifier("dbRealisation")
public class FilmDbStorage implements FilmStorage{

    private final JdbcTemplate jdbcTemplate;
    private final FilmDao filmDao;
    private final MpaDao mpaDao;
    private final LikesDao likesDao;
    private final GenresDao genresDao;


    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmDao filmDao,
                         MpaDao mpaDao, LikesDao likesDao, GenresDao genresDao){
        this.jdbcTemplate = jdbcTemplate;
        this.filmDao = filmDao;
        this.mpaDao = mpaDao;
        this.likesDao = likesDao;
        this.genresDao = genresDao;
    }

    @Override
    public List<Film> getAll() {
        return filmDao.getFilms();
    }

    @Override
    public Film create(Film film) {
        film.setId(getAll().size() + 1);
        Validator.validateFilm(film);
        mpaDao.validateMpa(film.getMpa());
        jdbcTemplate.update(insertFilmText(),
                film.getId(),film.getName(),
                film.getDescription(),film.getDuration(),film.getReleaseDate(), film.getMpa().getId(), film.getRate());
        if (film.getGenres() != null) {
            genresDao.updateFilmGenres(film.getId(),
                    film.getGenres().stream().map(Genre::getId).collect(Collectors.toList()));
            film.setGenres(genresDao.getFilmGenres(film.getId()));
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        Validator.validateFilm(film);
        filmDao.getFilmById(film.getId());
        mpaDao.validateMpa(film.getMpa());
        jdbcTemplate.update(updateFilmText(),
                film.getName(),film.getDescription(),
                film.getDuration(),film.getReleaseDate(),film.getMpa().getId(), film.getRate(), film.getId());
        if (film.getGenres() != null) {
            genresDao.updateFilmGenres(film.getId(),
                    film.getGenres().stream().map(Genre::getId).collect(Collectors.toList()));
            film.setGenres(genresDao.getFilmGenres(film.getId()));
        }
        return film;
    }

    @Override
    public List<Film> getFirst10LikedFilms(Integer count){
        List<Integer> idArray = jdbcTemplate.queryForList(getFirst10LikedFilms(), Integer.class, count);
        return filmDao.getFilms(idArray);
    }

    @Override
    public Optional<Film> getFilm(Integer filmId) {
        return filmDao.getFilmById(filmId);
    }

    @Override
    public Film addLike(Integer filmId, Long userId){
       return likesDao.addLike(filmId, userId);
    }

    @Override
    public Film removeLike(Integer filmId, Long userId){
        return likesDao.removeLike(filmId, userId);
    }

    private String insertFilmText(){
        return "INSERT INTO films (id, name, description, duration, releasedate, mpa, rate) " +
                "VALUES \n" +
                "(?,?,?,?,?,?,?)";
    }

    private String updateFilmText(){
        return "UPDATE FILMS SET \n" +
                "NAME = ?, DESCRIPTION = ?, DURATION = ?, RELEASEDATE = ?, MPA = ?, RATE = ? " +
                "WHERE ID = ?";
    }

    private String getFirst10LikedFilms(){

        return "SELECT ID \n" +
                "FROM FILMS AS F \n" +
                "LEFT JOIN \n" +
                "(SELECT \n" +
                "L.FILM_ID, COUNT(L.USER_ID) AS CLIKES \n" +
                "FROM LIKES AS L \n" +
                "GROUP BY FILM_ID) AS F_LIKES \n" +
                "ON F.ID = F_LIKES.FILM_ID \n" +
                "ORDER BY \n" +
                "F_LIKES.CLIKES DESC \n" +
                "LIMIT ?";
    }
}

