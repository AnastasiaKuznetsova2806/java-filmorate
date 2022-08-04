package ru.yandex.practicum.filmorate.util.sorting;

import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, SortingType> {

    @Override
    public SortingType convert(String source) {
        return SortingType.valueOf(source.toUpperCase());
    }
}
