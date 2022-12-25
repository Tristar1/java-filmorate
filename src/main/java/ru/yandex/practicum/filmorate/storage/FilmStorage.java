package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;

public interface FilmStorage {

    public List<Film> getAll();

    public Film create(Film film);

    public Film update(Film film);

    Film getFilm(Integer filmId);
}

