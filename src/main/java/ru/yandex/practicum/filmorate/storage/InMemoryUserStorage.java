package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Validated
@Component
public class InMemoryUserStorage extends User implements UserStorage {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryUserStorage.class);
    private final Map<Long, User> users = new HashMap<>();


    public User findById(Long id) {
        for (User user : users.values()) {
            if (user.getId() == id) {
                return user;
            }
        }
        return users.get(id);
    }


    public List<@Valid User> findAll() {
        LOG.info("Список всех пользователей представлен");
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
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

@Override
    public User update(User newUser) {
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
        throw new NotFoundException("Пользователь с ID " + newUser.getId() + " отсутствует!");
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

