package ru.yandex.practicum.filmorate.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.service.FilmUnvaliableException;
import ru.yandex.practicum.filmorate.service.UserUnvaliableException;

import java.util.Map;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(UserUnvaliableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final UserUnvaliableException exception) {
        return new ErrorResponse(exception.getMessage(), exception);
    }

    @ExceptionHandler(FilmUnvaliableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final FilmUnvaliableException exception) {
        return new ErrorResponse(exception.getMessage(), exception);
    }

    @ExceptionHandler(ValidationUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final ValidationUserException exception) {
        return new ErrorResponse(exception.getMessage(), exception);
    }

    @ExceptionHandler(ValidationFilmException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final ValidationFilmException exception) {
        return new ErrorResponse(exception.getMessage(), exception);
    }
}
