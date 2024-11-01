package ru.yandex.practicum.filmorate.storage;


import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> findAll();

    Film findById(Long id);

    Film create(@Valid Film film);

    Film update(@Valid Film newFilm);
}
