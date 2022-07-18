package ru.yandex.practicum.filmorate.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.referencebook.Genre;
import ru.yandex.practicum.filmorate.model.referencebook.Mpa;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class MpaAdapter extends JsonDeserializer<Film> {

    @Override
    public Film deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        long id = 0;
        if (node.get("id") != null) {
            id = node.get("id").asInt();
        }
        String name = node.get("name").textValue();
        LocalDate releaseDate = LocalDate.parse(node.get("releaseDate").textValue());
        String description = node.get("description").textValue();
        Integer duration = node.get("duration").asInt();

        Film film = new Film(name, description, releaseDate, duration);
        film.setId(id);

        JsonNode mpaJson = node.get("mpa");
        if (mpaJson != null && mpaJson.toString().contains("id")) {
            int mpaId = mpaJson.get("id").asInt();
            film.setMpa(new Mpa(mpaId));
        }

        JsonNode genresJson = node.get("genres");
        Set<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
        if (genresJson != null) {
            for (int i = 0; i < genresJson.size(); i++) {
                int genreId = genresJson.get(i).get("id").asInt();
                genres.add(new Genre(genreId));
            }
            film.setGenres(genres);
        }
        return film;
    }
}
