package ru.yandex.practicum.filmorate.controllers;

public class ErrorResponse {
    private final String errorMessage;

    public ErrorResponse(String text) {
        this.errorMessage = text;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
