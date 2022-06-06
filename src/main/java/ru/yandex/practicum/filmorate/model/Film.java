package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private int id;
    @NotNull(message = "Имя не может быть пустым")
    @NotBlank(message = "Имя не может быть пустым")
    private final String name;
    @Size(max = 200, message = "Длина описания не должна быть более 200 символов")
    private final String description;
    private final LocalDate releaseDate;
    @Min(value = 0, message = "Продолжительность фильма должна быть положительной")
    private final int duration;
}
