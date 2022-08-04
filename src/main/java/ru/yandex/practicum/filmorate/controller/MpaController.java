package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.referencebook.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@Slf4j
@RestController
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping(value = "/mpa")
    public Collection<Mpa> findAllMpa() {
        return mpaService.findAllMpa();
    }

    @GetMapping(value = "/mpa/{id}")
    public Mpa findMpaById(@PathVariable Integer id) {
        return mpaService.findMpaById(id);
    }
}
