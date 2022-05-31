package ru.yandex.practicum.filmorate.exceptions;

public class ValidationException extends RuntimeException{

    public ValidationException(String massage) {
        super(massage);
    }

    public String getMassage() {
        return super.getMessage();
    }
}
