package ru.yandex.practicum.filmorate.controllers;

import org.springframework.http.HttpStatus;

public class ValidationFilmException extends Exception {

    HttpStatus httpStatus;

    public ValidationFilmException(final String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus == null ? HttpStatus.BAD_REQUEST : httpStatus;
    }

}
