package ru.yandex.practicum.filmorate.model.referencebook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review{
    private long reviewId;
    @NotNull(message = "Поле content не может быть пустым")
    @NotBlank(message = "Поле content не может быть пустым")
    private String content;
    private Boolean isPositive;
    @NotNull(message = "Поле userId не может быть пустым")
    private Long userId;
    @NotNull(message = "Поле filmId не может быть пустым")
    private Long filmId;
    private int useful;
}
