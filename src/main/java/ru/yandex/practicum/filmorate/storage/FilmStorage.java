package ru.yandex.practicum.filmorate.storage;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    List<Film> getAll();

    Film create(Film film);

    Film update(Film film);

    Optional<Film> getFilm(Integer filmId);

    List<Film> getFirst10LikedFilms(Integer count);

    Film addLike(Integer idFilm, Long idUser);

    Film removeLike(Integer filmId, Long userId);

}

