package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User findById(Long userId) {
        return userStorage.findById(userId);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user); // Сохраняем через UserStorage
    }

    public User update(User newUser) {
        return userStorage.update(newUser); // Обновляем через UserStorage
    }


    public User addFriend(Long id, Long friendId) {
        if (id == null || friendId == null) {
            throw new NotFoundException("Один из пользователей не найден!");
        }

        User user = Optional.ofNullable(userStorage.findById(id))
                .orElseThrow(() -> new NotFoundException("Пользователь с Id " + id + " не найден!"));
        User friend = Optional.ofNullable(userStorage.findById(friendId))
                .orElseThrow(() -> new NotFoundException("Пользователь с Id " + friendId + " не найден!"));

        user.getFriends().add(friendId);
        friend.getFriends().add(id);

        userStorage.update(user);
        userStorage.update(friend);
        return user;
    }

    public User deleteFriend(Long userId, Long friendId) {
        if (userStorage.findById(userId) == null) {
            throw new NotFoundException("Пользователь с Id - " + userId + " не найден!");
        }
        if (userStorage.findById(friendId) == null) {
            throw new NotFoundException("Друг с Id - " + friendId + " не найден!");
        }

        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        if (friend == null) {
            throw new ValidationException("Человек, которого вы хотите удалить из друзей с Id " + friend + " не найден!");
        }

        if (user == null) {
            throw new ValidationException("Пользователь с Id - " + userId + " не найден!");
        }
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        return user;
    }


    public Set<Long> getFriends(Long id) {
        if (userStorage.findById(id) == null) {
            throw new NotFoundException("Пользователь с Id - " + id + " не найден!");
        }

        User user = userStorage.findById(id);
        if (user.getFriends() == null) {
            throw new NotFoundException("У пользователя с Id " + id + " нет друзей!");
        }

        return user.getFriends();
    }

    public Set<User> getCommonFriends(Long userId, Long otherId) {

        User user = Optional.ofNullable(userStorage.findById(userId))
                .orElseThrow(() -> new ValidationException("Пользователь с ID " + userId + " не найден"));
        User otherUser = Optional.ofNullable(userStorage.findById(otherId))
                .orElseThrow(() -> new ValidationException("Пользователь с ID " + otherId + " не найден"));

        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .map(userStorage::findById)
                .collect(Collectors.toSet());
    }

}
