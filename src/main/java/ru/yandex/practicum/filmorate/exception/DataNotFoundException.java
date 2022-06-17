package ru.yandex.practicum.filmorate.exception;

public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException(String massage) {
        super(massage);
    }

    public String getMassage() {
        return super.getMessage();
    }
}
