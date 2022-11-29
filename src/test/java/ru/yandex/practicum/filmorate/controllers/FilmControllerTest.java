package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import org.junit.jupiter.api.function.Executable;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private final FilmController filmController = new FilmController();

    @Test
    void validate() {

        // тест названия

        Film film = Film.builder()
                .id(1)
                .description("test")
                .duration(300)
                .name("")
                .releaseDate(LocalDate.of(2022,10,1))
                .build();

       ValidationFilmException exception = assertThrows(ValidationFilmException.class,generateExecutable(film));
       assertEquals("Название фильма не может быть пустым!.", exception.getMessage());

       // Тест описания
       String description = "";
       for (int i = 1; i<202; i++ ) {
           description = description + "A";
       }
       film.setName("Test");
       film.setDescription(description);

        exception = assertThrows(ValidationFilmException.class,generateExecutable(film));
        assertEquals("Длина описания фильма не может превышать 200 символов!", exception.getMessage());

        // Тест даты релиза
        film.setName("Test");
        film.setDescription("Description test");
        film.setReleaseDate(LocalDate.of(1895,12, 27));

        exception = assertThrows(ValidationFilmException.class,generateExecutable(film));
        assertEquals("Дата релиза не может быть меньше 28 декабря 1985 года!", exception.getMessage());

        // Тест длительности
        film.setReleaseDate(LocalDate.of(1895,12, 29));
        film.setDuration(-5);

        exception = assertThrows(ValidationFilmException.class,generateExecutable(film));
        assertEquals("Продолжительность фильма должна быть положительной!", exception.getMessage());

    }

    private Executable generateExecutable(Film film) {
        return () -> filmController.validateFilm(film);
    }

}