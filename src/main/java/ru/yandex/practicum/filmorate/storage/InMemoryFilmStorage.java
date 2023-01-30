package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmUnvaliableException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("inMemoryRealisation")
public class InMemoryFilmStorage implements FilmStorage{

    private final HashMap<Integer,Film> films = new HashMap<>();

    @Override
    public List<Film> getAll() {
        log.debug("Выполнен запрос get /film");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        film.setId(films.size() + 1);
        Validator.validateFilm(film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        Validator.validateFilm(film);
        film.setLikes(getFilm(film.getId()).orElseThrow().getLikes());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Optional<Film> getFilm(Integer filmId) {
        Film film = films.get(filmId);

        if (film == null) {
            throw new FilmUnvaliableException("Не найден фильм с ID = " + filmId);
        }

        return Optional.of(film);
    }

    @Override
    public List<Film> getFirst10LikedFilms(Integer count) {
        return getAll().stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());

    }

    @Override
    public Film addLike(Integer idFilm, Long idUser){
        getFilm(idFilm).orElseThrow().getLikes().add(idUser);
        return getFilm(idFilm).orElseThrow();
    }

    @Override
    public Film removeLike(Integer idFilm, Long idUser){
        getFilm(idFilm).orElseThrow().getLikes().remove(idUser);
        return getFilm(idFilm).orElseThrow();
    }

}
