package ru.yandex.practicum.filmorate.model.referencebook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Digits;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Mpa {
    @Digits(integer = 1, fraction = 5)
    private final int id;
    private String name;
}
