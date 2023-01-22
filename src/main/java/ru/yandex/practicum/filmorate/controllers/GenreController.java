package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
@Slf4j
@RestController
public class GenreController {

    private final GenresDao genresDao;

    public GenreController(GenresDao genresDao){

        this.genresDao = genresDao;

    }

    @GetMapping("/genres/{genreId}")
    public ResponseEntity<Genre> findById(@PathVariable int genreId) {
        return new ResponseEntity<>(genresDao.getGenreById(genreId), HttpStatus.OK);
    }

    @GetMapping("/genres")
    public ResponseEntity<List<Genre>> getAll() {
        return new ResponseEntity<>(genresDao.getGenres(), HttpStatus.OK);
    }
}
