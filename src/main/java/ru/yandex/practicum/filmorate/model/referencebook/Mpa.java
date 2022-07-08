package ru.yandex.practicum.filmorate.model.referencebook;

import lombok.Data;

import javax.validation.constraints.Digits;

@Data
public class Mpa {
    @Digits(integer = 1, fraction = 5)
    private final int id;
    private String name;

    public Mpa(int id) {
        this.id = id;
    }

    public Mpa(int id, String name) {
        this.id = id;
        this.name = name;
    }

}
