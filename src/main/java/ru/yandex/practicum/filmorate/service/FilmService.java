package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(@Qualifier("dbRealisation") FilmStorage filmStorage){
        this.filmStorage = filmStorage;
    }

    public Film addLike(Integer idFilm, Long idUser){
        return filmStorage.addLike(idFilm, idUser);
    }

    public Film removeLike(Integer idFilm, Long idUser){
        return filmStorage.removeLike(idFilm, idUser);
    }

    public List<Film> getFirst10LikedFilms(int count){
        return filmStorage.getFirst10LikedFilms(count);
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
        return filmStorage.getFilm(filmId).get();
    }

}
