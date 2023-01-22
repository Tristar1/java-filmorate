package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

public interface LikesDao {
    Film addLike(Integer filmId, Long userId);
    Film removeLike(Integer filmId, Long userId);
}
