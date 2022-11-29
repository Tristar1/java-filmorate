package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")
    public ResponseEntity create(@RequestBody User user) {
        try {
            user.setId(users.size()+1);
            validateUser(user);
            users.put(user.getId(), user);
            return new ResponseEntity<>(user,HttpStatus.CREATED);
        } catch (ValidationUserException exception) {

            return new ResponseEntity<>(user,exception.httpStatus);
        }
    }

    @PutMapping(value = "/users")
    public ResponseEntity update(@RequestBody User user) {
        try {
            validateUser(user);
            if (users.get(user.getId()) == null) {
                return new ResponseEntity<>(user,HttpStatus.NOT_FOUND);
            }
            users.put(user.getId(), user);
            return new ResponseEntity<>(user,HttpStatus.OK);
        } catch (ValidationUserException exception) {
            return new ResponseEntity<>(user,exception.httpStatus);
        }
    }

    public void validateUser(User user) throws ValidationUserException {

        String errorMessage;

        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя пользователя заменено на логин");
            user.setName(user.getLogin());
        }

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            errorMessage = "Почта не может быть пустой и должна включать символ @";
            log.debug(errorMessage);
            throw new ValidationUserException(errorMessage, null);
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            errorMessage = "Логин не может быть пустым и содержать пробелы!";
            log.debug(errorMessage);
            throw new ValidationUserException(errorMessage, null);
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            errorMessage = "Дата рождения пользователя не может быть больше текущей!";
            log.debug(errorMessage);
            throw new ValidationUserException(errorMessage, null);
        }
    }

}
