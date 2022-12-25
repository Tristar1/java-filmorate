package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    public UserController(UserStorage userStorage){
        this.userStorage = userStorage;
        this.userService = new UserService(userStorage);
    }

    @GetMapping("/users")
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> findById(@PathVariable long userId) {
        return new ResponseEntity<>(userStorage.getUser(userId), HttpStatus.OK);
    }

    @PostMapping(value = "/users")
    public ResponseEntity create(@RequestBody User user) {
        return new ResponseEntity<>(userStorage.create(user), HttpStatus.CREATED);
    }

    @PutMapping(value = "/users")
    public ResponseEntity update(@RequestBody User user) {
        return new ResponseEntity<>(userStorage.update(user), HttpStatus.OK);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public ResponseEntity addFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriend(id, friendId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public ResponseEntity removeFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.removeFriend(id, friendId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}/friends")
    public ResponseEntity getFriends(@PathVariable long id) {
        return new ResponseEntity<>(userService.getFriends(id),HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public ResponseEntity getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return new ResponseEntity<>(userService.getCommonFriends(id,otherId),HttpStatus.OK);
    }



}
