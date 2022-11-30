
package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class FilmController {
    private final HashMap<Integer,Film> films = new HashMap<>();

    @GetMapping("/films")
    public List<Film> getAll() {
        log.debug("Выполнен запрос get /film");
        return new ArrayList<>(films.values());
    }

    @PostMapping(value = "/films")
    public ResponseEntity create(@RequestBody Film film) {
        log.debug("Выполнен запрос PUT /film .");
        try {
            film.setId(films.size()+1);
            validateFilm(film);
            films.put(film.getId(), film);
            return new ResponseEntity<>(film,HttpStatus.CREATED);
        } catch (ValidationFilmException exception) {
            return new ResponseEntity<>(film,exception.httpStatus);
        }
    }

    @PutMapping(value = "/films")
    public ResponseEntity update(@RequestBody Film film) {
        try {
            validateFilm(film);
            if (films.get(film.getId()) == null) {
                return new ResponseEntity<>(film,HttpStatus.NOT_FOUND);
            }
            films.put(film.getId(), film);
            return new ResponseEntity<>(film,HttpStatus.OK);
        } catch (ValidationFilmException exception) {
            return new ResponseEntity<>(film,exception.httpStatus);
        }
    }

    public void validateFilm(Film film) throws ValidationFilmException {

        String errorMessage;

        if (film.getName().isBlank()) {
            errorMessage = "Название фильма не может быть пустым!.";
            log.debug(errorMessage);
            throw new ValidationFilmException(errorMessage, null);
        }
        if (film.getDescription().length() > 200) {
            errorMessage = "Длина описания фильма не может превышать 200 символов!";
            log.debug(errorMessage);
            throw new ValidationFilmException(errorMessage, null);
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            errorMessage = "Дата релиза не может быть меньше 28 декабря 1985 года!";
            log.debug(errorMessage);
            throw new ValidationFilmException(errorMessage, null);
        }
        if (film.getDuration() < 0) {
            errorMessage = "Продолжительность фильма должна быть положительной!";
            log.debug(errorMessage);
            throw new ValidationFilmException(errorMessage, null);
        }
    }

}

