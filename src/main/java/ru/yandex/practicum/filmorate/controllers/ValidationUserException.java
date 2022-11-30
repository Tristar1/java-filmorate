
package ru.yandex.practicum.filmorate.controllers;

import org.springframework.http.HttpStatus;

public class ValidationUserException extends Exception {

    HttpStatus httpStatus;

    public ValidationUserException(final String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus == null ? HttpStatus.BAD_REQUEST : httpStatus;
    }

}