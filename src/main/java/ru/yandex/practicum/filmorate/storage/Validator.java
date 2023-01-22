package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.controllers.ValidationFilmException;
import ru.yandex.practicum.filmorate.controllers.ValidationUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;

@Slf4j
public class Validator {

    public static void validateUser(User user) {

        String errorMessage;

        if (user.getFriendList() == null) {
            user.setFriendList(new HashSet<Long>());
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя пользователя заменено на логин");
            user.setName(user.getLogin());
        }

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            errorMessage = "Почта не может быть пустой и должна включать символ @";
            log.debug(errorMessage);
            throw new ValidationUserException(errorMessage);
        }

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            errorMessage = "Логин не может быть пустым и содержать пробелы!";
            log.debug(errorMessage);
            throw new ValidationUserException(errorMessage);
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            errorMessage = "Дата рождения пользователя не может быть больше текущей!";
            log.debug(errorMessage);
            throw new ValidationUserException(errorMessage);
        }

    }

    public static void validateFilm(Film film) {

        String errorMessage;

        if (film.getLikes() == null) {
            film.setLikes(new HashSet<Long>());
        }

        if (film.getRate() == null) {
            film.setRate(0);
        }

        if (film.getName().isBlank()) {
            errorMessage = "Название фильма не может быть пустым!.";
            log.debug(errorMessage);
            throw new ValidationFilmException(errorMessage);
        }
        if (film.getDescription().length() > 200) {
            errorMessage = "Длина описания фильма не может превышать 200 символов!";
            log.debug(errorMessage);
            throw new ValidationFilmException(errorMessage);
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            errorMessage = "Дата релиза не может быть меньше 28 декабря 1985 года!";
            log.debug(errorMessage);
            throw new ValidationFilmException(errorMessage);
        }
        if (film.getDuration() < 0) {
            errorMessage = "Продолжительность фильма должна быть положительной!";
            log.debug(errorMessage);
            throw new ValidationFilmException(errorMessage);
        }
    }
}
