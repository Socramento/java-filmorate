package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Validated
@Component
public class InMemoryFilmStorage extends Film implements FilmStorage {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private final Map<Long, Film> films = new HashMap<>();
    /**
     * Самая ранняя допустимая дата выпуска фильма (28 декабря 1895 года).
     */
    public static final LocalDate MOST_EARLE_DATE_RELEASE = LocalDate.of(1895, 12, 28);
    public static final int MAX_CHARACTERS = 200;

    /**
     * GET - запрос.
     */
    @Override
    public List<@Valid Film> findAll() {
        LOG.info("Список всех фильмов представлен");
        return new ArrayList<>(films.values());
    }

    public Film findById(Long id) {
        for (Film film : films.values()) {
            if (film.getId() == id) {
                return film;
            }
        }
        return films.get(id);
    }


    @Override
    public Film create(@Valid Film film) {

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

    @Override
    public Film update(@Valid Film newFilm) {
        if (newFilm == null) {
            throw new NotFoundException("Значение newFilm отсутствует");
        }

        if (newFilm.getId() == null) {
            LOG.warn("Не указан ID при внесении изменений через PUT-Film");
            throw new NotFoundException("Какое-то из полей не заполнено!");
        }


        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            if (oldFilm.getId() == null) {
                LOG.warn("Не указан ID при внесении изменений через PUT-Film");
                throw new NotFoundException("Какое-то из полей не заполнено!");
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
        throw new NotFoundException("Фильм с ID " + newFilm.getId() + " отсутствует!");
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

