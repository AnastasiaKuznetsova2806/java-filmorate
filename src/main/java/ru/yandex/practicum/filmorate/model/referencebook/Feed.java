package ru.yandex.practicum.filmorate.model.referencebook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feed {
    private long eventId;
    private long userId;
    private long entityId;
    private String eventType;
    private String operation;

    private long timestamp = Instant.now().toEpochMilli();

    public Feed(long userId, String eventType, String operation, long entityId) {
        this.userId = userId;
        this.eventType = eventType;
        this.operation = operation;
        this.entityId = entityId;
    }
}
