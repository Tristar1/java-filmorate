package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Component
@Slf4j
@RestController
public class FilmController {
    private final FilmService filmService;
    private final UserService userService;

    public FilmController(FilmStorage filmStorage, UserService userService){

        this.filmService = new FilmService(filmStorage);
        this.userService = userService;

    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        log.debug("Выполнен запрос get /film");
        return filmService.getAll();
    }

    @GetMapping("/films/{filmId}")
    public ResponseEntity<Film> findById(@PathVariable int filmId) {
        return new ResponseEntity<>(filmService.getFilm(filmId), HttpStatus.OK);
    }

    @PostMapping(value = "/films")
    public ResponseEntity<Film> create(@RequestBody Film film) {
        log.debug("Выполнен запрос PUT /film .");
        return new ResponseEntity<>(filmService.create(film), HttpStatus.CREATED);
    }

    @PutMapping(value = "/films")
    public ResponseEntity<Film> update(@RequestBody Film film) {
        return new ResponseEntity<>(filmService.update(film), HttpStatus.OK);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable Integer id, @PathVariable long userId) {
        userService.getUser(userId);
        return new ResponseEntity<>(filmService.addLike(id,userId), HttpStatus.OK);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public ResponseEntity<Film> removeLike(@PathVariable Integer id, @PathVariable long userId) {
        userService.getUser(userId);
        filmService.removeLike(id,userId);
        return new ResponseEntity<>(filmService.removeLike(id,userId), HttpStatus.OK);
    }

    @GetMapping("/films/popular")
    public ResponseEntity<List<Film>> getFirst10LikedFilms(@RequestParam(defaultValue = "10") Integer count) {
        return new ResponseEntity<>(filmService.getFirst10LikedFilms(count), HttpStatus.OK);
    }

}

