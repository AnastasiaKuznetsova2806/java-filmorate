package ru.yandex.practicum.filmorate.storage.user.feed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.referencebook.Feed;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FeedDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FeedDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Добавление в событий
    public void createFeed(Feed feed) {
        String sql = "INSERT INTO FEEDS(FEED_TIME, USER_ID, EVENT_TYPE, OPERATION, ENTITY_ID) " +
                "VALUES (?, ?, ?, ?, ?); ";

        jdbcTemplate.update(sql,
                feed.getTimestamp(),
                feed.getUserId(),
                feed.getEventType(),
                feed.getOperation(),
                feed.getEntityId());
    }

    public List<Feed> findAllFeedById(long id) {
        String sql = "select * from FEEDS where USER_ID = ?; ";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeFeed(rs)), id);
    }

    private Feed makeFeed(ResultSet rs) throws SQLException {
        long eventId = rs.getLong("FEED_ID");
        long userId = rs.getLong("USER_ID");
        long entityId = rs.getLong("ENTITY_ID");
        String eventType = rs.getString("EVENT_TYPE");
        String operation = rs.getString("OPERATION");
        long timestamp = rs.getLong("FEED_TIME");

        return new Feed(eventId, userId, entityId, eventType, operation, timestamp);
    }
}
