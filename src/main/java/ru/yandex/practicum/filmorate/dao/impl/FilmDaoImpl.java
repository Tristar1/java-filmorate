package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmUnvaliableException;

import java.util.*;

@Component
public class FilmDaoImpl implements FilmDao {
    private final Logger log = LoggerFactory.getLogger(FilmDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;
    private final GenresDao genresDao;

    public FilmDaoImpl(JdbcTemplate jdbcTemplate, MpaDao mpaDao, GenresDao genresDao){
        this.jdbcTemplate=jdbcTemplate;
        this.mpaDao = mpaDao;
        this.genresDao = genresDao;
    }

    @Override
    public Optional<Film> getFilmById(Integer filmId){
        Optional<Film> film = getFilms(filmId);
        if (film.isEmpty()) {
            throw new FilmUnvaliableException("Не найден фильм с ID = " + filmId);
        }
        return film;
    }

    @Override
    public List<Film> getFilms(List<Integer> filmsId){
        String parameters = String.join(",", Collections.nCopies(filmsId.size(), "?"));
        String filmsQuery = String.format(getSeveralFilmText(),parameters);
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(filmsQuery, filmsId.toArray());
        return filmsFromRowSet(filmRows);
    }

    public Optional<Film> getFilms(Integer filmId){
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(getOneFilmText(),filmId);
        return filmFromRowSet(filmRows);
    }

    public List<Film> getFilms(){
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(getFilmsText());
        return filmsFromRowSet(filmRows);
    }

    private Optional<Film> filmFromRowSet(SqlRowSet sqlRowSet) {
        List<Film> filmList = filmsFromRowSet(sqlRowSet);
        return (filmList.isEmpty()) ? Optional.empty()
                : Optional.of(filmList.get(0));
    }

    public List<Genre>getFilmGenres(Integer filmId){
        List<Integer> genreRows = jdbcTemplate.queryForList(getFilmGenresText(), Integer.class, filmId);
        return genresDao.getGenres(genreRows);
    }

    public List<Film> filmsFromRowSet(SqlRowSet sqlRowSet) {

        List<Film> filmList = new ArrayList<>();

        while (sqlRowSet.next()) {
            Film film = Film.builder()
                    .id(sqlRowSet.getInt("id"))
                    .name(sqlRowSet.getString("name"))
                    .description(sqlRowSet.getString("description"))
                    .duration(sqlRowSet.getInt("duration"))
                    .releaseDate(Objects.requireNonNull(sqlRowSet.getDate("releaseDate")).toLocalDate())
                    .mpa(mpaDao.getMpa(sqlRowSet.getInt("mpa")))
                    .rate(sqlRowSet.getInt("rate"))
                    .build();

            film.setGenres(new HashSet<>(genresDao.getFilmGenres(film.getId())));

            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            filmList.add(film);
        }
        return filmList;
    }

    private String getSeveralFilmText(){
        return "select * from films where id IN (%s)";
    }

    private String getOneFilmText(){
        return "select * from films where id = ?";
    }

    private String getFilmsText(){
        return "select * from films";
    }

    private String getFilmGenresText(){
        return "SELECT GENRE_ID FROM FILMS_GENRES WHERE FILM_ID = ?";
    }

}
