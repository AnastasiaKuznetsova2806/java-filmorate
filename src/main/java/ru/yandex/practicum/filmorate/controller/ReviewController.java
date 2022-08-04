package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.referencebook.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(final ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping(value = "/reviews")
    public Review createReview(@Valid @RequestBody Review review) {
        log.info("Получен запрос на добавление объекта: '{}'", review);
        return reviewService.createReview(review);
    }

    @PutMapping(value = "/reviews")
    public Review updateReview(@Valid @RequestBody Review review) {
        log.info("Получен запрос на обновление объекта: '{}'", review);
        return reviewService.updateReview(review);
    }

    @DeleteMapping(value = "/reviews/{id}")
    public void deleteReviewById(@PathVariable long id) {
        log.info("Получен запрос на отзыва фильма '{}'", id);
        reviewService.deleteReviewById(id);
    }

    @GetMapping(value = "/reviews/{id}")
    public Review findReviewById(@PathVariable long id) {
        return reviewService.findReviewById(id);
    }

    @GetMapping(value = "/reviews")
    public List<Review> findAllReviewsByFilmId(@RequestParam(defaultValue = "0") long filmId,
                                               @RequestParam(defaultValue = "10") int count) {
        return reviewService.findAllReviewsByFilmId(filmId, count);
    }

    @PutMapping(value = "/reviews/{id}/like/{userId}")
    public void addLikeReview(@PathVariable long id,
                              @PathVariable long userId) {
        log.info("Получен запрос на добавление лайка отзыву - '{}' от пользователя - '{}'", id, userId);
        reviewService.addLikeReview(id, userId);
    }

    @PutMapping(value = "/reviews/{id}/dislike/{userId}")
    public void addDislikeReview(@PathVariable long id,
                              @PathVariable long userId) {
        log.info("Получен запрос на добавление дизлайка отзыву - '{}' от пользователя - '{}'", id, userId);
        reviewService.addDislikeReview(id, userId);
    }

    @DeleteMapping(value = "/reviews/{id}/like/{userId}")
    public void deleteLikeReview(@PathVariable long id,
                              @PathVariable long userId) {
        log.info("Получен запрос на удаление лайка отзыву - '{}' от пользователя - '{}'", id, userId);
        reviewService.deleteLikeReview(id, userId);
    }

    @DeleteMapping(value = "/reviews/{id}/dislike/{userId}")
    public void deleteDislikeReview(@PathVariable long id,
                                 @PathVariable long userId) {
        log.info("Получен запрос на удаление дизлайка отзыву - '{}' от пользователя - '{}'", id, userId);
        reviewService.deleteDislikeReview(id, userId);
    }
}
