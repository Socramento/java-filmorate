package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

    FilmController filmController = new FilmController();
    Film film = new Film();
    UserController userController = new UserController();
    User user = new User();

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
            filmController.create(film);
            fail("Ожидалось исключение ValidationException для даты релиза ранее допустимой");
        } catch (ValidationException e) {
            Assertions.assertEquals(("Дата релиза ранее " + FilmController.MOST_EARLE_DATE_RELEASE), e.getMessage());
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
            filmController.create(film);
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
        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
    }
    @Test
    public void testNullDescription() {
        film.setName("Film 1");
        film.setDescription("");
        film.setReleaseDate(LocalDate.of(2012, 12, 12));
        film.setDuration(100L);
        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
    }
}
