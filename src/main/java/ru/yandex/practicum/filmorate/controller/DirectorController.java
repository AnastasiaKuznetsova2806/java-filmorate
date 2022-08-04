package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.referencebook.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class DirectorController {
    private final DirectorService directorService;

    @Autowired
    public DirectorController(final DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping(value = "/directors")
    public List<Director> findAllDirectors() {
        return directorService.findAllDirectors();
    }

    @GetMapping(value = "/directors/{id}")
    public Director findDirectorById(@PathVariable long id){
        return directorService.findDirectorById(id);
    }

    @PostMapping(value = "/directors")
    public Director createDirector(@Valid @RequestBody Director director) {
        log.info("Получен запрос на добавление объекта: '{}'", director);
        return directorService.createDirector(director);
    }

    @PutMapping(value = "/directors")
    public Director updateDirector(@Valid @RequestBody Director director) {
        log.info("Получен запрос на обновление объекта: '{}'", director);
        return directorService.updateDirector(director);
    }

    @DeleteMapping(value = "/directors/{id}")
    public void deletingDirectorById(@PathVariable long id) {
        log.info("Получен запрос на удаление режиссера: '{}'", id);
        directorService.deletingDirectorById(id);
    }
}
