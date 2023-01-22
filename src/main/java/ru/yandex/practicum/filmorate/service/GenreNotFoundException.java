package ru.yandex.practicum.filmorate.service;

public class GenreNotFoundException extends RuntimeException{
    public GenreNotFoundException(String message) {
        super(message);
    }
}
