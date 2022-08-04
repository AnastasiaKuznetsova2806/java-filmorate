package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.referencebook.Review;

import java.util.List;

public interface ReviewStorage {
    //Добавить отзыв
    Review createReview(Review review);

    //Обновить отзыв
    Review updateReview(Review review);

    //Удалить отзыв по уникальному идентификатору
    void deleteReviewById(long id);

    //Получить отзыв по уникальному идентификатору
    Review findReviewById(long id);

    //Получить все отзывы по идентификатору фильма
    List<Review> findAllReviewsByFilmId(long filmId);

    //Получить все отзывы
    List<Review> findAllReviews();
}
