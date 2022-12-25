package ru.yandex.practicum.filmorate.controllers;

public class ErrorResponse {
    String text;
    Exception exception;

    public ErrorResponse(String text, Exception exception) {
        this.text = text;
        this.exception = exception;
    }
}
