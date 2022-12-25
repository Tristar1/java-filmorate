package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    FilmStorage filmStorage;

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
}
