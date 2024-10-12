package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Validated
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Long, Film> films = new HashMap<>();
    public static final LocalDate MOST_EARLE_DATE_RELEASE = LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<@Valid Film> findAll() {
        log.info("Список всех фильмов представлен");
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Пустое название фильма.");
            throw new ValidationException("Описание фильма не может быть пусты!");
        }

        if (film.getDescription() == null || film.getDescription().isBlank()) {
            log.warn("Пустое название фильма.");
            throw new ValidationException("Описание фильма не может быть пусты!");
        }

        if (film.getDescription().length() > 200) {
            log.warn("Превышена максимальная длина (200 символов) - {}", film.getDescription().length());
            throw new ValidationException("Длина описания не должна превышать 200 символов.");
        }

        if (film.getReleaseDate().isBefore(MOST_EARLE_DATE_RELEASE)) {
            log.warn("Дата релиза ранее {}", MOST_EARLE_DATE_RELEASE);
            throw new ValidationException("Дата релиза ранее " + MOST_EARLE_DATE_RELEASE);
        }

        film.setId(getNextId());

        films.put(film.getId(), film);
        log.info("Фильм добавлен");
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            log.warn("Не указан ID при внесении изменений через PUT-Film");
            throw new ValidationException("Какое-то из полей не заполнено!");
        }


        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            if (oldFilm.getId() == null) {
                log.warn("Не указан ID при внесении изменений через PUT-Film");
                throw new ValidationException("Какое-то из полей не заполнено!");
            }

            oldFilm.setName(newFilm.getName());
            log.info("Называние изменено");
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Продолжительность изменена");
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            log.info("Дата релиза изменена");
            oldFilm.setDescription(newFilm.getDescription());
            log.info("Описание изменено");

            return oldFilm;
        }
        log.error("Фильм с id - {}  отсутствует!", newFilm.getId());
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


