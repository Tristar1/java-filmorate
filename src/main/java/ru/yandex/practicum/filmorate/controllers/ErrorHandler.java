package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.service.*;


@ControllerAdvice
public class ErrorHandler {

    HttpHeaders httpHeaders;
    ObjectMapper objectMapper;

    public ErrorHandler(){
        this.httpHeaders = new HttpHeaders();
        objectMapper = new ObjectMapper();
        httpHeaders.add("Content-Type", "application/json");
    }

    @ExceptionHandler(UserUnvaliableException.class)
    public ResponseEntity<String> handle(final UserUnvaliableException exception) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper.writeValueAsString(
                new ErrorResponse(exception.getMessage())),httpHeaders,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FilmUnvaliableException.class)
    public ResponseEntity<String> handle(final FilmUnvaliableException exception) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper.writeValueAsString(
                new ErrorResponse(exception.getMessage())),httpHeaders,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationUserException.class)
    public ResponseEntity<String> handle(final ValidationUserException exception) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper.writeValueAsString(
                new ErrorResponse(exception.getMessage())),httpHeaders,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationFilmException.class)
    public ResponseEntity<String> handle(final ValidationFilmException exception) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper.writeValueAsString(
                new ErrorResponse(exception.getMessage())),httpHeaders,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<String> handle(final UserAlreadyExistException exception) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper.writeValueAsString(
                new ErrorResponse(exception.getMessage())),httpHeaders,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MpaNotFoundException.class)
    public ResponseEntity<String> handle(final MpaNotFoundException exception) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper.writeValueAsString(
                new ErrorResponse(exception.getMessage())),httpHeaders,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GenreNotFoundException.class)
    public ResponseEntity<String> handle(final GenreNotFoundException exception) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper.writeValueAsString(
                new ErrorResponse(exception.getMessage())),httpHeaders,HttpStatus.NOT_FOUND);
    }
}
