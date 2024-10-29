package storage;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;


public interface UserStorage {

     ArrayList<@Valid User> findAll();

     User create(@Valid @RequestBody User user);

     User update(@Valid @RequestBody User newUser);
}
