package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationException extends ResponseStatusException {

    public ValidationException(HttpStatus status, String massage) {
        super(status, massage);
    }

    public String getMassage() {
        return super.getMessage();
    }
}
