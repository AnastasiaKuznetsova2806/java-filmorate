package ru.yandex.practicum.filmorate.storage.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.referencebook.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Component
public class ReviewDbStorage implements ReviewStorage{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review createReview(Review review) {
        String sql = "insert into REVIEWS(CONTENT, POSITIVE, FILM_ID, USER_ID) VALUES (?, ?, ?, ?); ";

        KeyHolder id = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedSt = connection.prepareStatement(sql, new String[]{"REVIEW_ID"});
            preparedSt.setString(1, review.getContent());
            preparedSt.setBoolean(2, review.getIsPositive());
            preparedSt.setLong(3, review.getFilmId());
            preparedSt.setLong(4, review.getUserId());
            return preparedSt;
        }, id);
        review.setReviewId(Objects.requireNonNull(id.getKey()).longValue());

        return review;
    }

    @Override
    public Review updateReview(Review review) {
        findReviewById(review.getReviewId());

        String sql = "update REVIEWS set CONTENT = ?, POSITIVE = ? where REVIEW_ID = ?; ";

        jdbcTemplate.update(sql,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());
        return review;
    }

    @Override
    public void deleteReviewById(long id) {
        findReviewById(id);

        String sql = "delete FROM REVIEWS where REVIEW_ID = ?; ";

        jdbcTemplate.update(sql, id);
    }

    @Override
    public Review findReviewById(long id) {
        String sql = "select R.REVIEW_ID, " +
                "       R.CONTENT, " +
                "       R.POSITIVE, " +
                "       R.USER_ID, " +
                "       R.FILM_ID, " +
                "       COALESCE(sum(RL.LIKE_DISLIKE), 0) as USEFUL " +
                "from REVIEWS R " +
                "left join REVIEW_LIKES RL on R.REVIEW_ID = RL.REVIEW_ID " +
                "where R.REVIEW_ID = ? " +
                "group by R.REVIEW_ID, R.CONTENT, R.POSITIVE, R.USER_ID, R.FILM_ID " +
                "order by USEFUL desc; ";

        Review review = jdbcTemplate.query(sql, rs -> rs.next() ? makeReview(rs) : null, id);

        if (review == null) {
            throw new DataNotFoundException(String.format("Отзыв %d не найден", id));
        }
        return review;
    }

    @Override
    public List<Review> findAllReviewsByFilmId(long filmId) {
        String sql = "select R.REVIEW_ID, " +
                "       R.CONTENT, " +
                "       R.POSITIVE, " +
                "       R.USER_ID, " +
                "       R.FILM_ID, " +
                "       COALESCE(sum(RL.LIKE_DISLIKE), 0) as USEFUL " +
                "from REVIEWS R " +
                "left join REVIEW_LIKES RL on R.REVIEW_ID = RL.REVIEW_ID " +
                "where FILM_ID = ? " +
                "group by R.REVIEW_ID, R.CONTENT, R.POSITIVE, R.USER_ID, R.FILM_ID " +
                "order by USEFUL desc; ";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeReview(rs)), filmId);
    }

    @Override
    public List<Review> findAllReviews() {
        String sql = "select R.REVIEW_ID, " +
                "       R.CONTENT, " +
                "       R.POSITIVE, " +
                "       R.USER_ID, " +
                "       R.FILM_ID, " +
                "       COALESCE(sum(RL.LIKE_DISLIKE), 0) as USEFUL " +
                "from REVIEWS R " +
                "left join REVIEW_LIKES RL on R.REVIEW_ID = RL.REVIEW_ID " +
                "group by R.REVIEW_ID, R.CONTENT, R.POSITIVE, R.USER_ID, R.FILM_ID " +
                "order by USEFUL desc; ";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeReview(rs)));
    }

    private Review makeReview(ResultSet rs) throws SQLException {
        int reviewId = rs.getInt("REVIEW_ID");
        String content = rs.getString("CONTENT");
        boolean isPositive = rs.getBoolean("POSITIVE");
        long userId = rs.getLong("USER_ID");
        long filmId = rs.getLong("FILM_ID");
        int useful = rs.getInt("USEFUL");
        return new Review(reviewId, content, isPositive, userId, filmId, useful);
    }
}
