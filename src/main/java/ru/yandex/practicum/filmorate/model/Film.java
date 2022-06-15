package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film implements Comparable<Film>{
    private long id;
    @NotNull(message = "Имя не может быть пустым")
    @NotBlank(message = "Имя не может быть пустым")
    private final String name;
    @Size(max = 200, message = "Длина описания не должна быть более 200 символов")
    private final String description;
    private final LocalDate releaseDate;
    @Min(value = 0, message = "Продолжительность фильма должна быть положительной")
    private final int duration;
    private Set<Long> likes;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
    }

    @Override
    public int compareTo(Film o) {
        return o.likes.size() - this.likes.size();
    }
}
