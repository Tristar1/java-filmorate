package ru.yandex.practicum.filmorate.service;

public class UserUnvaliableException extends RuntimeException{
    public UserUnvaliableException(String message) {
        super(message);
    }
}
