package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.List;

@Component
@Slf4j
@RestController
public class MpaController {

    private final MpaDao mpaDao;

    public MpaController(MpaDao mpaDao){

        this.mpaDao = mpaDao;

    }

    @GetMapping("/mpa/{mpaId}")
    public ResponseEntity<Mpa> findById(@PathVariable int mpaId) {
        return new ResponseEntity<>(mpaDao.getMpa(mpaId), HttpStatus.OK);
    }

    @GetMapping("/mpa")
    public ResponseEntity<List<Mpa>> getAll() {
        return new ResponseEntity<>(mpaDao.getMpa(), HttpStatus.OK);
    }
}
