package ru.yandex.practicum.filmorate.controllers;

public class ErrorResponse {
    private final String text;
    private final Exception exception;

    public ErrorResponse(String text, Exception exception) {
        this.text = text;
        this.exception = exception;
    }
}
