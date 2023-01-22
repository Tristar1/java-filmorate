package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.Validator;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private final InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    private final UserController userController = new UserController(inMemoryUserStorage);

    @Test
    void validate() {

        User user = User.builder()
                .id(1)
                .birthday(LocalDate.now().minusDays(50))
                .email("Serg@mail.ru")
                .login("Serg")
                .build();

        //Тест почты
        user.setEmail("");

        ValidationUserException exception = assertThrows(ValidationUserException.class,generateExecutable(user));
        assertEquals("Почта не может быть пустой и должна включать символ @", exception.getMessage());

        user.setEmail("Serg.ru");

        exception = assertThrows(ValidationUserException.class,generateExecutable(user));
        assertEquals("Почта не может быть пустой и должна включать символ @", exception.getMessage());

        // Тест логина

        user.setEmail("Serg@mail.ru");
        user.setLogin("f f");
        exception = assertThrows(ValidationUserException.class,generateExecutable(user));
        assertEquals("Логин не может быть пустым и содержать пробелы!", exception.getMessage());

        // Тест даты рождения
        user.setLogin("Serg");
        user.setBirthday(LocalDate.now().plusDays(1));

        exception = assertThrows(ValidationUserException.class,generateExecutable(user));
        assertEquals("Дата рождения пользователя не может быть больше текущей!", exception.getMessage());

    }

    private Executable generateExecutable(User user) {
        return () -> Validator.validateUser(user);
    }
}