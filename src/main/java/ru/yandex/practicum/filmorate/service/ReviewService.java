package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.referencebook.Feed;
import ru.yandex.practicum.filmorate.model.referencebook.Review;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.review.like.LikeReviewDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.feed.FeedDbStorage;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    public static final int LIKE = 1;
    public static final int DISLIKE = -1;
    private final ReviewStorage reviewStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeReviewDbStorage likeReviewStorage;
    private final FeedDbStorage feedStorage;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage,
                         FilmStorage filmStorage,
                         UserStorage userStorage,
                         LikeReviewDbStorage likeReviewStorage,
                         FeedDbStorage feedStorage) {
        this.reviewStorage = reviewStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeReviewStorage = likeReviewStorage;
        this.feedStorage = feedStorage;
    }

    //Добавление отзыва
    public Review createReview(Review review) {
        checkId(0L, review.getFilmId(), review.getUserId());

        Review newReview = reviewStorage.createReview(review);
        Feed feed = new Feed(newReview.getUserId(), "REVIEW", "ADD", newReview.getReviewId());
        feedStorage.createFeed(feed);
        return newReview;
    }

    //Обновление отзыва
    public Review updateReview(Review review) {
        checkId(0L, review.getFilmId(), review.getUserId());

        Review newReview = reviewStorage.updateReview(review);
        Feed feed = new Feed(newReview.getUserId(), "REVIEW", "UPDATE", newReview.getReviewId());
        feedStorage.createFeed(feed);
        return newReview;
    }

    //Удалуние отзывф по уникальному идентификатору
    public void deleteReviewById(long id) {
        checkId(id, null, null);
        Review review = findReviewById(id);

        reviewStorage.deleteReviewById(id);
        Feed feed = new Feed(review.getUserId(), "REVIEW", "REMOVE", review.getReviewId());
        feedStorage.createFeed(feed);
    }

    //Получуние отзыва по уникальному идентификатору
    public Review findReviewById(long id) {
        checkId(id, null, null);

        return reviewStorage.findReviewById(id);
    }

    //Получуние всех отзывов по идентификатору фильма
    public List<Review> findAllReviewsByFilmId(long filmId, int count) {
        if (filmId == 0) {
            return reviewStorage.findAllReviews().stream()
                    .limit(count)
                    .collect(Collectors.toList());
        } else {
            return reviewStorage.findAllReviewsByFilmId(filmId).stream()
                    .limit(count)
                    .collect(Collectors.toList());
        }
    }

    //Добавление лайка отзыву
    public void addLikeReview(long id, long userId) {
        checkId(id, null, userId);
        findReviewById(id);

        likeReviewStorage.addLikeDislikeReview(id, userId, LIKE);
    }

    //Добавление дизлайка отзыву
    public void addDislikeReview(long id, long userId) {
        checkId(id, null, userId);
        findReviewById(id);

        likeReviewStorage.addLikeDislikeReview(id, userId, DISLIKE);
    }

    //Удаление лайка отзыву
    public void deleteLikeReview(long id, long userId) {
        checkId(id, null, userId);
        findReviewById(id);

        likeReviewStorage.deleteLikeDislikeReview(id, userId, LIKE);
    }

    //Удаление дизлайка отзыву
    public void deleteDislikeReview(long id, long userId) {
        checkId(id, null, userId);
        findReviewById(id);

        likeReviewStorage.deleteLikeDislikeReview(id, userId, DISLIKE);
    }
    private void checkId(Long id, Long filmId, Long userId) {
        if (id == null) {
            throw new ValidationException("Поле пустое!");
        }

        if (filmId != null) {
            filmStorage.findFilmById(filmId);
        }

        if (userId != null) {
            userStorage.findUserById(userId);
        }
    }
}
