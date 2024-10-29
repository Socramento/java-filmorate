package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/films")
@Validated
public class FilmController {
    /**
     * filmService
     */
    private final FilmService filmService;
    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(FilmController.class);
    /**
     * Самая ранняя допустимая дата выпуска фильма (28 декабря 1895 года).
     */
    public static final LocalDate MOST_EARLE_DATE_RELEASE = LocalDate.of(1895, 12, 28);
    public static final int MAX_CHARACTERS = 200;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<@Valid Film> findAll() {
        LOG.info("Список всех фильмов представлен");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable("id") Long id) {
        return filmService.findById(id);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        return filmService.update(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Long id,
                          @PathVariable Long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping ("/{id}/like/{userId}")
    public String removeLike(@PathVariable Long id,
                             @PathVariable Long userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> topCountMostLikedFilms(@RequestParam (defaultValue = "10", required = false) int count) {
        return filmService.topCountMostLikedFilms(count);
    }

}


