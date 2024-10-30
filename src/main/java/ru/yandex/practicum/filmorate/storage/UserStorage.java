package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.model.User;
import java.util.List;


public interface UserStorage {
     List<User> findAll();
     User findById(Long id);
     User create(@Valid   User user);
     User update(@Valid  User newUser);
}
