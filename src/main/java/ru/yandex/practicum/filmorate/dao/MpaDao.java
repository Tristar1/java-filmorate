package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDao {
    Mpa getMpa(Integer mpaId);
    void validateMpa(Mpa mpa);
    List<Mpa> getMpa();

}
