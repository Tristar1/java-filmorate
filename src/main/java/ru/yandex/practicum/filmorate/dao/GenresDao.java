package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenresDao {
    Genre getGenreById(Integer genreId);
    List<Genre> getGenres();
    List<Genre> getGenres(List<Integer> genresId);
    Set<Genre> getFilmGenres(Integer filmId);
    Boolean updateFilmGenres(Integer filmId, List<Integer> genresId);

}
