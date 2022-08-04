package ru.yandex.practicum.filmorate.storage.review.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class LikeReviewDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLikeDislikeReview(long id, long userId, int likeDislike) {
        String sql = "insert into REVIEW_LIKES(REVIEW_ID, USER_ID, LIKE_DISLIKE) values (?, ?, ?); ";

        jdbcTemplate.update(sql, id, userId, likeDislike);
    }

    public void deleteLikeDislikeReview(long id, long userId, int likeDislike) {
        String sql = "delete from REVIEW_LIKES where REVIEW_ID = ? and USER_ID = ? and LIKE_DISLIKE = ?; ";

        jdbcTemplate.update(sql, id, userId, likeDislike);
    }
}
