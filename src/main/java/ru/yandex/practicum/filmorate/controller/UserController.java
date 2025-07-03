package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> usersMap = new HashMap<>();
    private int idController = 1;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        fillNameIfBlank(user);
        user.setId(idController++);
        usersMap.put(user.getId(), user);
        log.info("Пользователь создан: {}", user);
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Запрос на получение всех пользователей. Всего: {}", usersMap.size());
        return usersMap.values();
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (!usersMap.containsKey(user.getId())) {
            log.warn("Попытка обновить несуществующего пользователя с id: {}", user.getId());
            throw new ValidationException("Такого пользователя не существует");
        }
        fillNameIfBlank(user);
        usersMap.put(user.getId(), user);
        log.info("Пользователь обновлён: {}", user);
        return user;
    }

    private void fillNameIfBlank(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя не указано. Используем логин как имя: {}", user.getLogin());
        }
    }
}
