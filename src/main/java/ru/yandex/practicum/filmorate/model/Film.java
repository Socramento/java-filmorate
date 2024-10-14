package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Getter
@Setter
public class Film {

    private Long id;
    @NotNull(message = "Название не должно быть пустым")
    private String name;
    @NotNull(message = "Описание не должно быть пустым")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Значение должно быть положительным")
    private Long duration;
}
