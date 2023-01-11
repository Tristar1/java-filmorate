package ru.yandex.practicum.filmorate.controllers;

import org.springframework.http.HttpStatus;

public class ValidationFilmException extends RuntimeException {

    public ValidationFilmException(final String message) {
        super(message);
    }
}
