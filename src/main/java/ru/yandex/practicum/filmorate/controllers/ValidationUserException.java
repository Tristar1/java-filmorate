package ru.yandex.practicum.filmorate.controllers;

import org.springframework.http.HttpStatus;

public class ValidationUserException extends RuntimeException {

    public ValidationUserException(final String message) {
        super(message);
    }

}