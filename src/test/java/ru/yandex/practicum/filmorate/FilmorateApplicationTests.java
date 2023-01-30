package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;
	private final MpaDao mpaDao;
	private final GenresDao genresDao;

    @Test
    public void testFindUserById() {

       userStorage.create(User.builder()
                .login("test")
                .birthday(LocalDate.now())
                .name("test")
                .email("mail@mail.ru")
                .build());

        Optional<User> userOptional = userStorage.getUser(Long.parseLong("1"));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", Long.parseLong("1"))
                );
    }

	@Test
	public void testFindFilmById() {

		filmStorage.create(Film.builder()
				.name("nisi eiusmod")
				.releaseDate(LocalDate.now())
				.name("film")
				.description("description")
				.duration(100)
				.mpa(mpaDao.getMpa(1))
				.genres(new HashSet<>(List.of(genresDao.getGenreById(1))))
				.build());

		Optional<Film> userOptional = filmStorage.getFilm(1);

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);
	}
}

