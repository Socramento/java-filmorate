package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class FilmorateApplicationTests {


    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    User user = new User();
    Film film = new Film();

    @Test
    public void testNameAndDescriptionFilm() {

        film.setName("Film 1");
        film.setDescription("About Film 1");
        Assertions.assertEquals("Film 1", film.getName());
        Assertions.assertEquals("About Film 1", film.getDescription());

    }

    @Test
    public void testDateReleaseValidationFilm() {

        film.setName("Film 1");
        film.setDescription("About Film 1");
        film.setReleaseDate(LocalDate.of(1700, 12, 29));
        film.setDuration(100L);
        try {
            inMemoryFilmStorage.create(film);
            fail("Ожидалось исключение ValidationException для даты релиза ранее допустимой");
        } catch (ValidationException e) {
            Assertions.assertEquals(("Дата релиза ранее " + InMemoryFilmStorage.MOST_EARLE_DATE_RELEASE), e.getMessage());
        }
    }

    @Test
    public void testLengthDescriptionFilm() {
        film.setName("Film 1");
        film.setDescription("33333333333333333333333333333333333333333333333333333333333333333" +
                "33333333333333333333333333333333333333333333333333333333333333333333333333333" +
                "33333333333333333333333333333333333333333333333333333333333333333333333333333");
        film.setReleaseDate(LocalDate.of(2000, 12, 29));
        film.setDuration(100L);

        try {
            inMemoryFilmStorage.create(film);
            fail("Ожидалось исключение ValidationException для длины описания фильма более 200 символов");
        } catch (ValidationException e) {
            Assertions.assertEquals("Длина описания не должна превышать 200 символов.", e.getMessage());
        }
    }

    @Test
    public void testNullNameFilm() {
        film.setName(null);
        film.setDescription("About Film 1");
        film.setReleaseDate(LocalDate.of(2012, 12, 12));
        film.setDuration(100L);
        Assertions.assertThrows(ValidationException.class, () -> inMemoryFilmStorage.create(film));
    }

    @Test
    public void testNullDescriptionFilm() {
        film.setName("Film 1");
        film.setDescription("");
        film.setReleaseDate(LocalDate.of(2012, 12, 12));
        film.setDuration(100L);
        Assertions.assertThrows(ValidationException.class, () -> inMemoryFilmStorage.create(film));
    }

    @Test
    public void testNullLoginUser() {
        user.setName("Bob");
        user.setLogin(null);
        user.setEmail("bob@gmail.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Assertions.assertThrows(ValidationException.class, () -> inMemoryUserStorage.create(user));
    }

}
