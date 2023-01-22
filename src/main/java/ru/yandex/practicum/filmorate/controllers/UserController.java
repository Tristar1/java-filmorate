package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.List;


@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    public UserController(@Qualifier("dbRealisation") UserStorage userStorage){
        this.userService = new UserService(userStorage);
    }

    @GetMapping("/users")
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> findById(@PathVariable long userId) {
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @PostMapping(value = "/users")
    public ResponseEntity<User> create(@RequestBody User user) {
        return new ResponseEntity<>(userService.create(user), HttpStatus.CREATED);
    }

    @PutMapping(value = "/users")
    public ResponseEntity<User> update(@RequestBody User user) {
        return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public ResponseEntity<User> addFriend(@PathVariable long id, @PathVariable long friendId) {
        return new ResponseEntity<>(userService.addFriend(id, friendId), HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public ResponseEntity<User> removeFriend(@PathVariable long id, @PathVariable long friendId) {
        return new ResponseEntity<>(userService.removeFriend(id, friendId), HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable long id) {
        return new ResponseEntity<>(userService.getFriends(id),HttpStatus.OK);
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return new ResponseEntity<>(userService.getCommonFriends(id,otherId),HttpStatus.OK);
    }



}
