package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Validated
public class FilmController {
    private static final Logger LOG = LoggerFactory
            .getLogger(FilmController.class);
    private final Map<Long, Film> films = new HashMap<>();
    /**
     * Самая ранняя допустимая дата выпуска фильма (28 декабря 1895 года).
     */
    public static final LocalDate MOST_EARLE_DATE_RELEASE = LocalDate.of(1895, 12, 28);
    public static final int MAX_CHARACTERS = 200;

    /**
     * GET - запрос.
     */
    @GetMapping
    public ArrayList<@Valid Film> findAll() {
        LOG.info("Список всех фильмов представлен");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody  Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            LOG.warn("Пустое название фильма.");
            throw new ValidationException("Описание фильма не может быть пусты!");
        }

        if (film.getDescription() == null || film.getDescription().isBlank()) {
            LOG.warn("Пустое название фильма.");
            throw new ValidationException("Описание фильма не может быть пусты!");
        }

        if (film.getDescription().length() > MAX_CHARACTERS) {
            LOG.warn("Превышена максимальная длина (200 символов) - {}", film.getDescription().length());
            throw new ValidationException("Длина описания не должна превышать 200 символов.");
        }

        if (film.getReleaseDate().isBefore(MOST_EARLE_DATE_RELEASE)) {
            LOG.warn("Дата релиза ранее {}", MOST_EARLE_DATE_RELEASE);
            throw new ValidationException("Дата релиза ранее " + MOST_EARLE_DATE_RELEASE);
        }

        film.setId(getNextId());

        films.put(film.getId(), film);
        LOG.info("Фильм добавлен");
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            LOG.warn("Не указан ID при внесении изменений через PUT-Film");
            throw new ValidationException("Какое-то из полей не заполнено!");
        }


        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            if (oldFilm.getId() == null) {
                LOG.warn("Не указан ID при внесении изменений через PUT-Film");
                throw new ValidationException("Какое-то из полей не заполнено!");
            }

            oldFilm.setName(newFilm.getName());
            LOG.info("Называние изменено");
            oldFilm.setDuration(newFilm.getDuration());
            LOG.info("Продолжительность изменена");
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            LOG.info("Дата релиза изменена");
            oldFilm.setDescription(newFilm.getDescription());
            LOG.info("Описание изменено");

            return oldFilm;
        }
        LOG.error("Фильм с id - {}  отсутствует!", newFilm.getId());
        throw new ValidationException("Фильм с ID " + newFilm.getId() + " отсутствует!");
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}


