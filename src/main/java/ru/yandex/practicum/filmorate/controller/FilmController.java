package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    public HashMap<Integer, Film> filmsMap = new HashMap<>();
    private int idCounter = 1;

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Попытка добавить фильм с недопустимой датой релиза: {}", film.getReleaseDate());
            throw new ValidationException("Фильм не добавлен: дата релиза раньше 28.12.1895");
        }
        film.setId(idCounter++);
        filmsMap.put(film.getId(), film);
        log.info("Фильм добавлен: {}", film);
        return film;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Запрос на получение всех фильмов. Всего: {}", filmsMap.size());
        return filmsMap.values();
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!filmsMap.containsKey(film.getId())) {
            log.warn("Попытка обновить несуществующий фильм с id: {}", film.getId());
            throw new ValidationException("Фильм с таким ID не найден");
        }
        filmsMap.put(film.getId(), film);
        log.info("Фильм обновлён: {}", film);
        return film;
    }
}
