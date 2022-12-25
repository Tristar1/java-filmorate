package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Component
@Slf4j
@RestController
public class FilmController {

    FilmStorage filmStorage;
    FilmService filmService;
    UserStorage userStorage;

    public FilmController(FilmStorage filmStorage, UserStorage userStorage){

        this.filmStorage = filmStorage;
        this.filmService = new FilmService(filmStorage);
        this.userStorage = userStorage;

    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        log.debug("Выполнен запрос get /film");
        return filmStorage.getAll();
    }

    @GetMapping("/films/{filmId}")
    public ResponseEntity<Film> findById(@PathVariable int filmId) {
        return new ResponseEntity<>(filmStorage.getFilm(filmId), HttpStatus.OK);
    }

    @PostMapping(value = "/films")
    public ResponseEntity create(@RequestBody Film film) {
        log.debug("Выполнен запрос PUT /film .");
        return new ResponseEntity<>(filmStorage.create(film), HttpStatus.CREATED);
    }

    @PutMapping(value = "/films")
    public ResponseEntity update(@RequestBody Film film) {
        return new ResponseEntity<>(filmStorage.update(film), HttpStatus.OK);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public ResponseEntity addLike(@PathVariable Integer id, @PathVariable long userId) {
        userStorage.getUser(userId);
        return new ResponseEntity<>(filmService.addLike(id,userId), HttpStatus.OK);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public ResponseEntity removeLike(@PathVariable Integer id, @PathVariable long userId) {
        userStorage.getUser(userId);
        filmService.removeLike(id,userId);
        return new ResponseEntity<>(filmService.removeLike(id,userId), HttpStatus.OK);
    }

    @GetMapping("/films/popular")
    public ResponseEntity getFirst10LikedFilms(@RequestParam(defaultValue = "10") Integer count) {
        return new ResponseEntity<>(filmService.getFirst10LikedFilms(count), HttpStatus.OK);
    }

}

