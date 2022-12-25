package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.ValidationFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmUnvaliableException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{

    private final HashMap<Integer,Film> films = new HashMap<>();

    @Override
    public List<Film> getAll() {
        log.debug("Выполнен запрос get /film");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        film.setId(films.size() + 1);
        validateFilm(film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        validateFilm(film);
        film.setLikes(getFilm(film.getId()).getLikes());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilm(Integer filmId) {
        Film film = films.get(filmId);

        if (film == null) {
            throw new FilmUnvaliableException("Не найден фильм с ID = " + filmId);
        }

        return film;
    }

    public void validateFilm(Film film) {

        String errorMessage;

        if (film.getLikes() == null) {
            film.setLikes(new HashSet<Long>());
        }

        if (film.getName().isBlank()) {
            errorMessage = "Название фильма не может быть пустым!.";
            log.debug(errorMessage);
            throw new ValidationFilmException(errorMessage);
        }
        if (film.getDescription().length() > 200) {
            errorMessage = "Длина описания фильма не может превышать 200 символов!";
            log.debug(errorMessage);
            throw new ValidationFilmException(errorMessage);
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            errorMessage = "Дата релиза не может быть меньше 28 декабря 1985 года!";
            log.debug(errorMessage);
            throw new ValidationFilmException(errorMessage);
        }
        if (film.getDuration() < 0) {
            errorMessage = "Продолжительность фильма должна быть положительной!";
            log.debug(errorMessage);
            throw new ValidationFilmException(errorMessage);
        }
    }

}
