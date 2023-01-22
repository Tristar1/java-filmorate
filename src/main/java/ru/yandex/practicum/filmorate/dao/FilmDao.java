package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface FilmDao {
    Optional<Film> getFilmById(Integer filmId);

    List<Film> getFilms(List<Integer> filmsId);

    List<Film> getFilms();
    List<Genre> getFilmGenres(Integer filmId);

}
