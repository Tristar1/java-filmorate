package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmUnvaliableException;
import ru.yandex.practicum.filmorate.service.GenreNotFoundException;

import java.util.*;

@Component
@Qualifier("dbRealisation")
public class GenresDaoImpl implements GenresDao {

    private final Logger log = LoggerFactory.getLogger(FilmDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public GenresDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(Integer genreId) {
        Optional<Genre> genre = getGenres(genreId);
        if (genre.isEmpty()) {
            throw new FilmUnvaliableException("Не найден жанр с ID = " + genreId);
        }
        return genre.get();
    }

    @Override
    public Boolean updateFilmGenres(Integer filmId, List<Integer> genresId) {
        jdbcTemplate.update(deleteGenresText(), filmId);
        String insertText = updateFilmGenresText();
        if (!genresId.isEmpty()) {
            for (Integer genreId : new ArrayList<>(new HashSet<>(genresId))) {
                insertText = insertText + "(" + filmId + "," + genreId + "),\n";
            }
            jdbcTemplate.update(insertText.substring(0, insertText.length() - 2));
        }
        return true;
    }

    @Override
    public Set<Genre> getFilmGenres(Integer filmId) {
        List<Integer> genresId = jdbcTemplate.queryForList(getFilmGenresText(), Integer.class, filmId);
        return new HashSet<>(getGenres(genresId));
    }

    @Override
    public List<Genre> getGenres(List<Integer> genresId) {
        String parameters = String.join(",", Collections.nCopies(genresId.size(), "?"));

        String filmsQuery = String.format(getSeveralgenreText(), parameters);

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(filmsQuery, genresId.toArray());

        return genresFromRowSet(filmRows);
    }

    public Optional<Genre> getGenres(Integer genreId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(getOneGenreText(), genreId);
        Optional<Genre> genre = genreFromRowSet(filmRows);
        if (genre.isEmpty()) {
            throw new GenreNotFoundException("Жанр с ID " + genreId + " не обнаружен!");
        }
        return genre;
    }

    public List<Genre> getGenres() {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(getGenresText());
        return genresFromRowSet(filmRows);
    }

    private Optional<Genre> genreFromRowSet(SqlRowSet sqlRowSet) {
        List<Genre> genreList = genresFromRowSet(sqlRowSet);
        return (genreList.isEmpty()) ? Optional.empty()
                : Optional.of(genreList.get(0));
    }

    public List<Genre> genresFromRowSet(SqlRowSet sqlRowSet) {

        List<Genre> genreList = new ArrayList<>();

        while (sqlRowSet.next()) {
            Genre genre = Genre.builder()
                    .id(sqlRowSet.getInt("id"))
                    .name(sqlRowSet.getString("name"))
                    .build();

            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());
            genreList.add(genre);
        }
        return genreList;
    }

    private String getSeveralgenreText() {
        return "select * from genres where id IN (%s)";
    }

    private String getFilmGenresText() {
        return "SELECT DISTINCT GENRE_ID FROM FILMS_GENRES WHERE FILM_ID = ? ORDER BY GENRE_ID";
    }

    private String getOneGenreText() {
        return "select * from genres where id = ?";
    }

    private String getGenresText() {
        return "select * from genres";
    }

    private String deleteGenresText() {
        return "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
    }

    private String updateFilmGenresText() {
        return "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) \n" +
                "VALUES \n";
    }

}
