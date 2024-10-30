package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable("id") Long id) {
        return userService.findById(id);  // Используем UserService
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();  // Используем UserService
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);  // Используем UserService для создания
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        return userService.update(newUser);  // Используем UserService для обновления
    }


    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id, @PathVariable  Long friendId) {
        return userService.addFriend(id, friendId);
    }


    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getFriends(@PathVariable Long id) {
       return userService.getFriends(id);
    }

    @GetMapping ("/{id}/friends/common/{otherId}")
    public Set<User> getFriendsCommon(@PathVariable Long id,
                                      @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId);

    }
}

