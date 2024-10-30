package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(Long id) {
        return filmStorage.findById(id);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film addLike(Long filmId, Long userId) {
        Film likedFilm = Optional.ofNullable(filmStorage.findById(filmId)).orElseThrow(() -> new NotFoundException("Фильма с Id - " + filmId + " не найден!"));
        User user = Optional.ofNullable(userStorage.findById(userId)).orElseThrow(() -> new NotFoundException("Пользователь с Id - " + userId + " не найден!"));

        likedFilm.getLikes().add(userId);
        return likedFilm;
    }

    public String removeLike(Long filmId, Long userId) {
        Film likedFilm = Optional.ofNullable(filmStorage.findById(filmId)).orElseThrow(() -> new NotFoundException("Фильма с Id - " + filmId + " не найден!"));
        User user = Optional.ofNullable(userStorage.findById(userId)).orElseThrow(() -> new NotFoundException("Пользователь с Id - " + userId + " не найден!"));

        likedFilm.getLikes().remove(userId);

        return "Пользователь c ID - " + userId + " удалил Лайк, фильму под номером " + filmId;
    }

    public List<Film> topCountMostLikedFilms(int count) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
