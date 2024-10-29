package storage;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

public interface FilmStorage {

     ArrayList<@Valid Film> findAll();

     Film create(@Valid @RequestBody Film film);

     Film update(@Valid @RequestBody Film newFilm);

}
