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
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final Map<Long, User> users = new HashMap<>();


    @GetMapping
    public Collection<@Valid User> findAll() {
        LOG.info("Список всех пользователей представлен");
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            LOG.warn("Не заполнен ЛОГИН!");
            throw new ValidationException("Не заполнен логин или в нем присутствую пробелы");
        }

        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            LOG.info("Имя заполнено.");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            LOG.warn("Не указан EMAIL!");
            throw new ValidationException("Не указан EMAIL!");
        }

        user.setId(getNextId());
        users.put(user.getId(), user);
        LOG.info("Пользователь добавлен");
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());

            oldUser.setName(newUser.getName());
            LOG.info("Имя изменено");
            oldUser.setLogin(newUser.getLogin());
            LOG.info("Логин изменен");
            oldUser.setBirthday(newUser.getBirthday());
            LOG.info("День рождения изменен ");
            oldUser.setEmail(newUser.getEmail());
            LOG.info("Email изменен");

            return oldUser;
        }
        LOG.warn("Пользователь с id -  {}  отсутствует!", newUser.getId());
        throw new ValidationException("Пользователь с ID " + newUser.getId() + " отсутствует!");
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
