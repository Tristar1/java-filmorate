package ru.yandex.practicum.filmorate.service;

public class MpaNotFoundException extends RuntimeException{
    public MpaNotFoundException(String message) {
        super(message);
    }
}
