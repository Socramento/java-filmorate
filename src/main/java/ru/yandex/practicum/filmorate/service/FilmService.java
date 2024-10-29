package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
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

        Film likedFilm = filmStorage.findById(filmId);
        User user = userService.findById(userId);

        if (likedFilm == null) {throw new ValidationException("Фильма с Id - " + filmId + " не найден!");}
        if (user == null) {throw new ValidationException("Пользователь с Id - " + userId + " не найден!");}

        likedFilm.getLikes().add(userId);

        return likedFilm;
    }

    public String removeLike(Long filmId, Long userId) {
        Film likedFilm = filmStorage.findById(filmId);
        User user = userService.findById(userId);

        if (likedFilm == null) {
            throw new ValidationException("Фильма с Id - " + filmId + " не существует!");
        }

        if (user == null) {
            throw new ValidationException("Пользователь с Id - " + userId + " не существует!");
        }

        likedFilm.getLikes().remove(userId);


        return "Пользователь c ID - " + userId + " удалил Лайк, фильму под номером " + filmId;
    }


    public List<Film> topCountMostLikedFilms(int count) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size())) // Сортировка по количеству лайков
                .limit(count)
                .collect(Collectors.toList());
    }

}
