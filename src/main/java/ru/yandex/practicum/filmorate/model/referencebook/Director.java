package ru.yandex.practicum.filmorate.model.referencebook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Director {
    private long id;
    @NotNull(message = "Имя режиссера не может быть пустым")
    @NotBlank(message = "Имя режиссера не может быть пустым")
    private String name;

    public Director(long id) {
        this.id = id;
    }
}
