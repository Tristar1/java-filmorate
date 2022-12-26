package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage){
        this.filmStorage = filmStorage;
    }

    public Film addLike(Integer idFilm, long idUser){
        filmStorage.getFilm(idFilm).getLikes().add(idUser);
        return filmStorage.getFilm(idFilm);
    }

    public Film removeLike(Integer idFilm, long idUser){
        filmStorage.getFilm(idFilm).getLikes().remove(idUser);
        return filmStorage.getFilm(idFilm);
    }

    public List<Film> getFirst10LikedFilms(int count){
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }
    public Film create(Film film) {
        return filmStorage.create(film);
    }
    public Film update(Film film) {
        return filmStorage.update(film);
    }
    public Film getFilm(Integer filmId) {
        return filmStorage.getFilm(filmId);
    }

}
